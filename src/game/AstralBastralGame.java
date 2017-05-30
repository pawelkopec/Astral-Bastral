package game;

import server.AccessPoint;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;


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
    private final static float MAX_X = 512.0f;
    private final static float MAX_Y = 512.0f;
    private final static float MIN_X = -512.0f;
    private final static float MIN_Y = -512.0f;

    // State update parameters.
    private static final int REFRESH_BYTES_OFFSET = 60;

    // Constant turrets coordinates and starting rotation.
    private static final float[] TURRET_XS = {32.0f, 32.0f, -32.0f, -32.0f};
    private static final float[] TURRET_YS = {32.0f, -32.0f, -32.0f, 32.0f};
    private static final float STARTING_ROTATION = 0f;

    // Constant empty player index and empty turret rotation.
    private static final int EMPTY_PLAYER_INDEX = -1;
    private static final float EMPTY_ROTATION = -1.0f;

    // Constant int size.
    private static final int INT_SIZE = 4;

    // Constant index to main ship.
    private static final int MAIN_SHIP_INDEX = 0;

    // Score assigned for killing one enemy ship.
    private static final int KILL_SCORE = 10;

    private boolean gameActive = false;

    // Arrays of all in-game entities, players and their scores.
    private Vector<Player> players;
    private int [] scores;
    private Vector<GameEntity> entities;

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
    private int updateIndex;

    // Game random generator.
    protected static Random randomGenerator;

    // Indicator of defeat.
    private boolean defeat = false;


    public AstralBastralGame() {
        players = new Vector<>(MAX_PLAYERS);
        scores = new int[MAX_PLAYERS];
        entities = new Vector<>(MAX_ENTITIES);

        for (int i = 0; i < MAX_PLAYERS; i++) {
            players.add(null);
        }

        for (int i = 0; i < MAX_ENTITIES; i++) {
            entities.add(null);
        }

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
        updateIndex = 0;

        randomGenerator = new Random();
        addEntity(new MainShip(0, 0, 0));
    }

    @Override
    public void performAction(byte[] args, int playerId) {

        // Rotate turret assigned to player.
        Action action = new Action(args, playerId);
        int turretIndex = players.get(playerId).getTurretIndex();
        ((Turret) entities.get(turretIndex)).rotate(action.getRotation());

        Missile missile;

        // Spawn all missiles created with this action.
        for (int i = 0; i < action.getMissilesToSpawn().length; i++) {
            missile = action.getMissilesToSpawn()[i];
            if (missile == null || freeIndices.empty()) {
                break;
            }
            addEntity(missile);
        }

    }

    @Override
    // Shouldn't it return player id???
    public int addPlayer(AccessPoint accessPoint) {
        Turret playerTurret;
        int turretIndex;
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (players.get(i) == null) {

                // If there is not space in entities array remove first
                // encountered asteroid, missile or enemy space ship.
                if (freeIndices.empty()) {
                    for (int j = 0; j < MAX_ENTITIES; j++) {
                        if (
                            entities.get(j).getType() != GameEntitiesTypes.TURRET &&
                            entities.get(j).getType() != GameEntitiesTypes.MAIN_SHIP
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

                players.set(i, new Player(i, turretIndex, accessPoint));
                scores[i] = 0;
                return i;
            }
        }
        return -1;
    }

    @Override
    public void removePlayer(int playerId) {
        // Remove turret associated with player.
        removeEntity(players.get(playerId).getTurretIndex());
        players.set(playerId, null);
    }

    @Override
    public void setActive(boolean active) {
        gameActive = active;
    }

    @Override
    public boolean isActive() {
        return gameActive;
    }

    @Override
    public void makeTurn(float deltaTime) {
        boolean[] collisionWhiteList;
        GameEntity createdEntity;
        // First stage: action of all entities and spawning.
        spawnObstacles();
        for (int i = 0; i < MAX_ENTITIES; i++) {
            // If there is space in entities array left.
            if (entities.get(i) != null && !freeIndices.empty()) {
                createdEntity = entities.get(i).act();
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
            if (entities.get(i) != null) {
                entities.get(i).move(deltaTime);
                if (
                    entities.get(i).getX() > MAX_X || entities.get(i).getX() < MIN_X ||
                    entities.get(i).getY() > MAX_Y || entities.get(i).getY() < MIN_Y
                ) {
                    removeEntity(i);
                }
            }
        }
        // Third stage: check collision between entities and remove these,
        // which die as a result of it.
        for (int i = 0; i < MAX_ENTITIES; i++) {
            if (entities.get(i) != null) {
                collisionWhiteList = entities.get(i).getCollisionWhiteList();
                for (int j = i + 1; j < MAX_ENTITIES; j++) {
                    if (
                        entities.get(j) != null && entities.get(j).isActive() &&
                        collisionWhiteList[entities.get(j).getType().getValue()] &&
                        entities.get(i).isCollidingWith(entities.get(j))
                    ) {
                        entities.get(i).collideWith(entities.get(j));
                        entities.get(j).collideWith(entities.get(i));
                    }
                }
                if (!entities.get(i).isActive()) {
                    if (
                        entities.get(i).getType() ==
                        GameEntitiesTypes.ENEMY_SHIP &&
                        ((EnemyShip) entities.get(i)).getKillerIndex() !=
                        EnemyShip.NO_KILLER
                    ) {
                        scores[((EnemyShip) entities.get(i)).getKillerIndex()] +=
                            KILL_SCORE;
                    }
                    removeEntity(i);
                }
            }
        }
    }

    // Spawn enemy ships and/or asteroids.
    private void spawnObstacles() {
        float ASTEROID_SPAWN_PROBABILITY = 0.04f;
        float ENEMY_SHIP_SPAWN_PROBABILITY = 0.01f;
        float MIN_X = 400.0f;
        float MIN_Y = 400.0f;
        float asteroidSpawn = randomGenerator.nextFloat();
        float enemyShipSpawn = randomGenerator.nextFloat();
        if (asteroidSpawn < ASTEROID_SPAWN_PROBABILITY) {
            // Get random x and y outside of 800 x 800 square centered at
            // (0, 0).
            float x = randomGenerator.nextFloat() * 2 * MAX_X - MAX_X;
            float y = randomGenerator.nextFloat() * 2 * MAX_Y - MAX_Y;
            // Check if x, y outside of bounds.
            if (Math.abs(x) > MIN_X || Math.abs(y) > MIN_Y) {
                float rotation = (float) Math.PI * randomGenerator.nextFloat() / 2;
                rotation -= (Math.PI * 5.0 / 4.0 - Math.atan2(y, x));
                addEntity(new Asteroid(x, y, rotation));
            }
        }
        if (enemyShipSpawn < ENEMY_SHIP_SPAWN_PROBABILITY) {
            // Get random x and y outside of 800 x 800 square centered at
            // (0, 0).
            float x = randomGenerator.nextFloat() * 2 * MAX_X - MAX_X;
            float y = randomGenerator.nextFloat() * 2 * MAX_Y - MAX_Y;
            // Randomly negate x and/or y.
            if (Math.abs(x) > MIN_X || Math.abs(y) > MIN_Y) {
                addEntity(new EnemyShip(x, y));
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
            if (players.get(i) != null) {
                bytes = ByteBuffer.allocate(INT_SIZE).putInt(i).array();
                System.arraycopy(bytes, 0, update, 0, INT_SIZE);
                try {
                    players.get(i).sendUpdate(update);
                }
                catch (IOException exception) {
                    // TODO
                }
            }
        }

        // Change entities array state update window index.
        stateRefreshIndex = (stateRefreshIndex + 1) %
        STATE_REFRESH_WINDOW_SIZE;
        updateIndex += 1;

    }

    // Simple method used to add entity to the array and creation list.
    // It returns index at which entity was added to entities array.
    private int addEntity(GameEntity entity) {
        int freeIndex = freeIndices.pop();
        entities.set(freeIndex, entity);
        createdEntities.add(entity);
        createdEntitiesIndices.add(freeIndex);

        return freeIndex;
    }

    // Simple method used to remove entity from the array and add its array
    // index to stack of unused array indices.
    private void removeEntity(int index) {
        if (entities.get(index).getType() == GameEntitiesTypes.MAIN_SHIP) {
            defeat = true;
        }
        entities.set(index, null);
        freeIndices.push(index);
        destructionIndices.add(index);
    }

    private byte[] getStateUpdate() throws IOException {

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(byteStream);

        // Get state update entities array window bytes.
        byteStream.reset();
        for (int i = 0; i < STATE_REFRESH_SIZE; i++) {
            if (entities.get(stateRefreshIndex * STATE_REFRESH_SIZE + i) != null) {
                entities.get(
                    stateRefreshIndex * STATE_REFRESH_SIZE + i
                ).writeTo(output);
            }
            else {
                output.writeShort(GameEntitiesTypes.EMPTY_ENTITY.getValue());
            }
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
        output.writeInt(updateIndex);
        if (defeat) {
            output.writeInt(0);
        }
        else {
            output.writeInt(((MainShip) entities.get(MAIN_SHIP_INDEX)).getHp());
        }
        output.writeInt(REFRESH_BYTES_OFFSET);
        output.writeInt(REFRESH_BYTES_OFFSET + createdEntitiesBytes.length);
        output.writeInt(
            REFRESH_BYTES_OFFSET + createdEntitiesBytes.length +
            destroyedEntitiesBytes.length
        );
        output.writeInt(stateRefreshIndex);
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (players.get(i) != null) {
                output.writeFloat(
                    entities.get(players.get(i).getTurretIndex()).getRotation()
                );
            }
            else {
                output.writeFloat(EMPTY_ROTATION);
            }
        }
        for (int i = 0; i < MAX_PLAYERS; i++) {
            if (players.get(i) != null) {
                output.writeInt(scores[i]);
            }
            else {
                output.writeInt(0);
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

        output.flush();

        return byteStream.toByteArray();
    }


}
