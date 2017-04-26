package server;

import game.Action;
import game.Game;

import java.io.IOException;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * A runnable that serves a player.
 */
public class ClientWorker implements Runnable{

    private Game game;
    private AccessPoint accessPoint;
    private int id;

    public ClientWorker(Game game, AccessPoint accessPoint, int id) {
        this.game = game;
        this.accessPoint = accessPoint;
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Action action = game.parseAction(accessPoint.getData());
                game.performAction(action, id);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public AccessPoint getAccessPoint() {
        return accessPoint;
    }
}
