package server;

import game.Game;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * A runnable that waits for new players and
 * and adds them to game.
 */
public class ClientConnectionManager implements Runnable {

    private Game game;

    public ClientConnectionManager(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        //TODO
    }
}
