package main;

import game.AstralBastralGame;
import game.Game;
import game.GameMaster;
import server.ClientConnectionManager;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        List<Integer> ports = new LinkedList<>();
        ports.add(8081);
        ports.add(8082);
        ports.add(8083);
        ports.add(8084);
        Game game = new AstralBastralGame();
        game.setActive(true);
        new Thread(new ClientConnectionManager(game, ports, 9090, null)).start();
        new GameMaster(game, 30).run();
    }
}
