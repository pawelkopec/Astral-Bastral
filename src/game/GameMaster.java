package game;

import server.ClientConnectionManager;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * A class that controls access to a game.
 */
public class GameMaster {

    private static final String ILLEGAL_UPDATE_TIME = "Update time must be positive.";

    private static final int DEFAULT_UPDATE_TIME = 200;

    private Game game;
    private int updateTime;

    public GameMaster(Game game, int updateTime) {
        if (updateTime <= 0) {
            throw new IllegalArgumentException(ILLEGAL_UPDATE_TIME);
        }

        this.game = game;
        this.updateTime = updateTime;
    }

    public GameMaster(Game game) {
        this(game, DEFAULT_UPDATE_TIME);
    }

    public void init() {
        //ClientConnectionManager manager = new ClientConnectionManager(game);
        //TODO manager setup
        //new Thread(manager).start();
    }

    public void gameLoop() throws InterruptedException {
        int timeDivider = 1000;
        long delta, lastLoopTime = System.currentTimeMillis();
        while (game.isActive()) {
            delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();
            Thread.sleep(updateTime);
            game.makeTurn((float) delta / timeDivider);
            game.sendUpdates();
        }
    }

}
