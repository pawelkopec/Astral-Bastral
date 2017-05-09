package game;

import server.AccessPoint;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * Main Astral Bastral game class which implements Game interface.
 */
public class AstralBastralGame implements Game {

    // Limits for number of game entities present in-game at once and for
    // number of players.
    private final static int MAX_PLAYERS = 4;
    private final static int MAX_ENTITIES = 1024;

    // Number of entities send with single state refresh and last refresh
    // window index;
    private static final int STATE_REFRESH_SIZE = 64;
    private static final int STATE_REFRESH_WINDOW_SIZE = 16;

    // Game field boundaries.
    private final static float MAX_X = 1024.0f;
    private final static float MAX_Y = 1024.0f;
    private final static float MIN_X = -1024.0f;
    private final static float MIN_Y = -1024.0f;

    // State update parameters.
    private static final int REFRESH_BYTES_OFFSET = 32;

    // Constant turrets coordinates and starting rotation.
    private static final float[] TURRET_XS = {25.0f, 25.0f, -25.0f, -25.0f};
    private static final float[] TURRET_YS = {25.0f, -25.0f, -25.0f, 25.0f};
    private static final float STARTING_ROTATION = 0f;

    // Constant empty player index and empty turret rotation.
    private static final int EMPTY_PLAYER_INDEX = -1;
    private static final float EMPTY_ROTATION = 0.0f;

    // Constant int size.
    private static final int INT_SIZE = 4;


    // Arrays of all in-game entities and players.
    private Player [] players;
    private GameEntity [] entities;

    // Stack of unoccupied indices in entities array.
    private Stack<Integer> freeIndices;

    // Lists of entities created and destroyed in current game tick. List for
    // destroyed entities does not contain whole entities, but only their
    // indices, because that is all what is needed to destroy particular
    // entity at both client and server sides.
    private List<GameEntity> createdEntities;
    private List<Integer> createdEntitiesIndices;
    private List<Integer> destructionIndices;

    // Index of current state refresh window in entities array.
    private int stateRefreshIndex;


    public AstralBastralGame() {
        players = new Player[MAX_PLAYERS];
        entities = new GameEntity[MAX_ENTITIES];

        // Create and initialize stack of free indices with all possible
        // indices.
        freeIndices = new Stack<>();
        for (int i = MAX_ENTITIES - 1; i >= 0; i--) {
            freeIndices.push(i);
        }

        // Create lists for created and destroyed entities.
        createdEntities = new ArrayList<>();
        createdEntitiesIndices = new ArrayList<>();
        destructionIndices = new ArrayList<>();

        stateRefreshIndex = 0;
    }

    @Override
    public void performAction(Action action, int playerId) {

        // Rotate turret assigned to player.
        int turretIndex = players[playerId].getTurretIndex();
        ((Turret) entities[turretIndex]).rotate(action.getRotation());

        Missile missile;

        // Spawn all missiles created with this action.
        for (int i = 0; i < action.getMissilesToSpawn().length; i++) {
            missile = action.getMissilesToSpawn()[i];
            if (missile != null && !freeIndices.empty()) {
                addEntity(missile);
            }
            else {
                break;
            }
        }

    }

    @Override
    // Shouldn't it return player id???
    public int addPlayer(AccessPoint accessPoint) {
        Turret playerTurret;
        int turretIndex;
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (players[i] == null) {

                // If there is not space in entities array remove first
                // encountered asteroid, missile or enemy space ship.
                if (freeIndices.empty()) {
                    for (int j = 0; j < MAX_ENTITIES; j++) {
                        if (
                            entities[j].getType() != GameEntitiesTypes.TURRET &&
                            entities[j].getType() != GameEntitiesTypes.MAIN_SHIP
                        ) {
                            removeEntity(j);
                            break;
                        }
                    }
                }

                // Create turret which will be assigned to player.
                playerTurret = new Turret(
                    i, TURRET_XS[i], TURRET_YS[i], STARTING_ROTATION
                );
                turretIndex = addEntity(playerTurret);

                players[i] = new Player(i, turretIndex, accessPoint);
                return i;
            }
        }
        return -1;
    }

    @Override
    public void removePlayer(int playerId) {

        // Remove turret associated with player.
        int turretIndex = players[playerId].getTurretIndex();
        removeEntity(turretIndex);

        players[playerId] = null;
    }

    @Override
    public Action parseAction(byte[] args) {

        // It might be prettier with action parsing in this method
        // instead of the Action constructor.
        return new Action(args);

    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void makeTurn() {
        boolean[] collisionWhiteList;
        GameEntity createdEntity;

        // First stage: action of all entities.
        for (int i = 0; i < MAX_ENTITIES; i++) {

            // If there is space in entities array left.
            if (entities[i] != null && !freeIndices.empty()) {
                createdEntity = entities[i].act();
                if (createdEntity != null) {
                    addEntity(createdEntity);
                }
            }
            // If there is no space left in entities array stop loop.
            else if (freeIndices.empty()) {
                break;
            }

        }

        // Second stage: move all entities. If they exit game field delete them
        // from entities array.
        for (int i = 0; i < MAX_ENTITIES; i++) {
            if (entities[i] != null) {
                entities[i].move();
                if (
                    entities[i].getX() > MAX_X || entities[i].getX() < MIN_X ||
                    entities[i].getY() > MAX_Y || entities[i].getY() < MIN_Y
                ) {
                    removeEntity(i);
                }
            }
        }

        // Third stage: check collision between entities and remove these,
        // which die as a result of it.
        for (int i = 0; i < MAX_ENTITIES; i++) {
            if (entities[i] != null) {
                collisionWhiteList = entities[i].getCollisionWhiteList();
                for (int j = i + 1; j < MAX_ENTITIES; j++) {
                    if (
                        entities[j] != null && entities[j].isActive() &&
                        collisionWhiteList[entities[j].getType().getValue()] &&
                        entities[i].isCollidingWith(entities[j])
                    ) {
                        entities[i].collideWith(entities[j]);
                        entities[j].collideWith(entities[i]);
                    }
                }
                if (!entities[i].isActive()) {
                    removeEntity(i);
                }
            }
        }

    }

    @Override
    public void sendUpdates() {

        byte[] update = null;
        try {
            update = getStateUpdate();
        }
        catch (IOException exception) {
            // TODO
        }

        // Send updates to all players.
        byte[] bytes;
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (players[i] != null) {
                bytes = ByteBuffer.allocate(INT_SIZE).putInt(i).array();
                for (int j = 0; j < INT_SIZE; j++) {
                    update[j] = bytes[j];
                }
                try {
                    players[i].sendUpdate(update);
                }
                catch (IOException exception) {
                    // TODO
                }
            }
        }

        // Change entities array state update window index.
        stateRefreshIndex = (stateRefreshIndex + 1) %
        STATE_REFRESH_WINDOW_SIZE;

    }

    // Simple method used to add entity to the array and creation list.
    // It returns index at which entity was added to entities array.
    private int addEntity(GameEntity entity) {
        int freeIndex = freeIndices.pop();
        entities[freeIndex] = entity;
        createdEntities.add(entity);
        createdEntitiesIndices.add(freeIndex);
        return freeIndex;
    }

    // Simple method used to remove entity from the array and add its array
    // index to stack of unused array indices.
    private void removeEntity(int index) {
        entities[index] = null;
        freeIndices.push(index);
    }

    private byte[] getStateUpdate() throws IOException {

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(byteStream);

        // Get state update entities array window bytes.
        byteStream.reset();
        for (int i = 0; i < STATE_REFRESH_SIZE; i++) {
            entities[
                stateRefreshIndex * STATE_REFRESH_SIZE + i
            ].writeTo(output);
        }
        output.flush();
        byte[] stateRefreshBytes = byteStream.toByteArray();

        // Get created entities bytes and their indices.
        byteStream.reset();
        for (int i = 0; i < createdEntities.size(); i++) {
            output.writeInt(createdEntitiesIndices.get(i));
            createdEntities.get(i).writeTo(output);
        }
        output.flush();
        byte[] createdEntitiesBytes = byteStream.toByteArray();

        // Get destroyed entities indices.
        byteStream.reset();
        for (int index : destructionIndices) {
            output.writeInt(index);
        }
        output.flush();
        byte[] destroyedEntitiesBytes = byteStream.toByteArray();

        // Output offsets of state update sectors and other necessary data.
        byteStream.reset();
        output.writeInt(EMPTY_PLAYER_INDEX);
        output.writeInt(REFRESH_BYTES_OFFSET);
        output.writeInt(REFRESH_BYTES_OFFSET + createdEntitiesBytes.length);
        output.writeInt(
            REFRESH_BYTES_OFFSET + createdEntitiesBytes.length +
            destroyedEntitiesBytes.length
        );
        output.writeInt(stateRefreshIndex);
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (players[i] != null) {
                output.writeFloat(
                    entities[players[i].getTurretIndex()].getRotation()
                );
            }
            else {
                output.writeFloat(EMPTY_ROTATION);
            }
        }

        // Output state update body: refresh, created entities and destroyed
        // indices bytes.
        output.write(createdEntitiesBytes);
        output.write(destroyedEntitiesBytes);
        output.write(stateRefreshBytes);

        // Clear lists of created and destroyed entities.
        createdEntities.clear();
        createdEntitiesIndices.clear();
        destructionIndices.clear();

        return new byte[0];
    }


}
