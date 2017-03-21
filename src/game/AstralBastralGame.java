package game;

/**
 * Created by Paweł Kopeć on 21.03.17.
 */
public class AstralBastralGame implements Game {
    @Override
    public void performAction(Action action, long playerId) {

    }

    @Override
    public byte[] getStateUpdate(long playerId) {
        return new byte[0];
    }

    @Override
    public Action parseAction(byte[] args) {
        return null;
    }
}
