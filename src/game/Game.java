package game;

import server.AccessPoint;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * Interface for game.
 */
public interface Game {

    int FAILURE = -1;

    void performAction(Action action, int playerId);

    int addPlayer(AccessPoint accessPoint);

    void removePlayer(int playerId);

    Action parseAction(byte[] args);

    void setActive(boolean active);

    boolean isActive();

    void makeTurn();

    void sendUpdates();

}
