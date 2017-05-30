package server;

import game.Game;

import java.io.IOException;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * A runnable that serves a player.
 */
class ClientWorker implements Runnable{

    private static final int TIMEOUT = 30000;

    private Game game;
    private AccessPoint accessPoint;
    private ClientConnectionManager manager;
    private int playerId;
    private boolean running;

    ClientWorker(Game game, AccessPoint accessPoint, ClientConnectionManager manager, int playerId) {
        this.game = game;
        this.accessPoint = accessPoint;
        this.manager = manager;
        this.playerId = playerId;
    }

    @Override
    public void run() {
        Counter counter = new Counter(TIMEOUT, () -> stop());
        new Thread(counter).start();
        running = true;
        try {
            while (game.isActive() && running) {
                game.performAction(accessPoint.getData(), playerId);
                counter.reset();
            }
        } catch (IOException ignored) {
            System.out.println("Player of id " + playerId + " lost connection.");
        } finally {
            stop();
        }
    }

    private synchronized void stop() {
        if (running) {
            running = false;
            game.removePlayer(playerId);
            manager.destroyAccessPoint(accessPoint);

            //TODO logger
            System.out.println("Player of id " + playerId + " removed.");
        }
    }
}
