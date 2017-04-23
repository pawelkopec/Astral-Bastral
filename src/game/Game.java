package game;

import server.AccessPoint;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * Interface for game.
 */
public interface Game {

    void performAction(Action action, long playerId);

    byte[] getStateUpdate(long playerId);

    void addPlayer(AccessPoint accessPoint);

    void removePlayer(long playerId);

    Action parseAction(byte[] args);

    boolean isActive();

    void makeTurn();

    void sendUpdates();

}
