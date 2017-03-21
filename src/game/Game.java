package game;

/**
 * Created by Paweł Kopeć on 21.03.17.
 */
public interface Game {

    void performAction(Action action, long playerId);

    byte[] getStateUpdate(long playerId);

    Action parseAction(byte[] args);
}
