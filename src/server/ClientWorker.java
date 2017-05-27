package server;

import game.Action;
import game.Game;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * A runnable that serves a player.
 */
class ClientWorker implements Runnable{

    private Game game;
    private AccessPoint accessPoint;
    private ClientConnectionManager manager;
    private int id;

    ClientWorker(Game game, AccessPoint accessPoint, ClientConnectionManager manager, int id) {
        this.game = game;
        this.accessPoint = accessPoint;
        this.manager = manager;
        this.id = id;
    }

    @Override
    public void run() {
        //TODO managing socket failures
        while (game.isActive()) {
            try {
                game.performAction(accessPoint.getData(), id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        manager.destroyAccessPoint(accessPoint);
    }

    public AccessPoint getAccessPoint() {
        return accessPoint;
    }
}
