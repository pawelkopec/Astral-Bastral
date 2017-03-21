package server;

import game.Game;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * A runnable that serves a player.
 */
public class ClientWorker implements Runnable{

    private Game game;
    private AccessPoint accessPoint;

    public ClientWorker(Game game, AccessPoint accessPoint) {
        this.game = game;
        this.accessPoint = accessPoint;
    }

    @Override
    public void run() {

    }

    public AccessPoint getAccessPoint() {
        return accessPoint;
    }
}
