package game;

import server.AccessPoint;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * Interface for game.
 */
public interface Game {

    void performAction(Action action, int playerId);

    void addPlayer(AccessPoint accessPoint);

    void removePlayer(int playerId);

    Action parseAction(byte[] args);

    boolean isActive();

    void makeTurn();

    void sendUpdates();

}
