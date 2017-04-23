package game;

import server.AccessPoint;

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

    // Game field boundaries.
    private final static float MAX_X = (float) 1024.0;
    private final static float MAX_Y = (float) 1024.0;
    private final static float MIN_X = (float) -1024.0;
    private final static float MIN_Y = (float) -1024.0;


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
    private List<Integer> destructionIndices;


    public AstralBastralGame() {
        players = new Player[MAX_PLAYERS];
        entities = new GameEntity[MAX_ENTITIES];

        // Create and initialize stack of free indices with all possible
        // indices.
        freeIndices = new Stack<>();
        for (int i = MAX_ENTITIES - 1; i >= 0; i--) {
            freeIndices.push(i);
        }

        createdEntities = new ArrayList<>();
        destructionIndices = new ArrayList<>();
    }

    @Override
    public void performAction(Action action, long playerId) {

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

    public byte[] getStateUpdate() {

        return new byte[0];
    }

    @Override
    public void addPlayer(AccessPoint accessPoint) {

    }

    @Override
    public void removePlayer(long playerId) {

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

        // First stage: move all entities. If they exit game field delete them
        // from entities array.
        for (int i = 0; i < MAX_ENTITIES; i++) {
            entities[i].move();
            if (
                entities[i].getX() > MAX_X || entities[i].getX() < MIN_X ||
                entities[i].getY() > MAX_Y || entities[i].getY() < MIN_Y
            ) {
                removeEntity(i);
            }
        }

        // Second stage: check collision between entities and remove these,
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

        // Third stage: action of all entities.
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

    }

    @Override
    public void sendUpdates() {


        // Clear lists of created and destroyed entities.
        createdEntities.clear();
        destructionIndices.clear();

    }

    // Simple method used to add entity to the array and creation list.
    private void addEntity(GameEntity entity) {
        int freeIndex = freeIndices.pop();
        entities[freeIndex] = entity;
        createdEntities.add(entity);
    }

    // Simple method used to remove entity from the array and add its array
    // index to stack of unused array indices.
    private void removeEntity(int index) {
        entities[index] = null;
        freeIndices.push(index);
    }

}
