package main;

import game.AstralBastralGame;
import server.ClientConnectionManager;
import server.PortManager;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        List<Integer> ports = new LinkedList<>();
        ports.add(8081);
        ports.add(8082);
        ports.add(8083);
        ports.add(8084);
        PortManager manager = new PortManager(ports, 2);
        new Thread(new ClientConnectionManager(new AstralBastralGame(), manager, 9090, null)).start();
    }
}
