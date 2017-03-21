package game;

import server.AccessPoint;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * Astral Bastral yeah.
 */
public class AstralBastralGame implements Game {

    private final static int MAX_PLAYERS = 4;
    private Player [] players = new Player[MAX_PLAYERS];


    public AstralBastralGame() {
        for (int i = 0; i < MAX_PLAYERS; i++) {
            players[i] = null;
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

        return null;
    }
}
