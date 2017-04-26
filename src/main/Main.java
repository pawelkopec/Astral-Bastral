package main;

import game.AstralBastralGame;
import server.ClientConnectionManager;
import server.PortManager;

import java.io.IOException;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) {
        LinkedList<Integer> ports = new LinkedList<>();
        ports.add(9094);
        ports.add(9091);
        ports.add(9092);
        ports.add(9093);
        PortManager manager = new PortManager(ports, 4);
        try {
            new Thread(new ClientConnectionManager(new AstralBastralGame(), manager, 9090)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
