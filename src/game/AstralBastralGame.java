package game;

import server.AccessPoint;

import java.util.Stack;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * Astral Bastral yeah.
 */
public class AstralBastralGame implements Game {

    private final static int MAX_PLAYERS = 4;
    private final static int MAX_ENTITIES = 1024;

    private final static float MAX_X = (float) 1024.0;
    private final static float MAX_Y = (float) 1024.0;
    private final static float MIN_X = (float) -1024.0;
    private final static float MIN_Y = (float) -1024.0;


    private Player [] players;
    private GameEntity [] entities;
    private Stack<Integer> creationIndices;
    private Stack<Integer> destructionIndices;


    public AstralBastralGame() {
        players = new Player[MAX_PLAYERS];
        entities = new GameEntity[MAX_ENTITIES];
        creationIndices = new Stack<Integer>();
        for (int i = MAX_ENTITIES - 1; i >= 0; i--) {
            creationIndices.push(i);
        }
    }

    @Override
    public void performAction(Action action, long playerId) {

    }

    @Override
    public byte[] getStateUpdate(long playerId) {
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

    public void makeTurn() {
        boolean[] collisionWhiteList;

        for (int i = 0; i < MAX_ENTITIES; i++) {
            entities[i].move();
            if (
                entities[i].getX() > MAX_X || entities[i].getX() < MIN_X ||
                entities[i].getY() > MAX_Y || entities[i].getY() < MIN_Y
            ) {
                removeEntity(i);
            }
        }

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

    public void sendUpdates() {

    }

    private void removeEntity(int index) {
        entities[index] = null;
        creationIndices.push(index);
    }

}
