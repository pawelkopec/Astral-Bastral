package server;

import game.Action;
import game.Game;

import java.io.IOException;
import java.net.*;
import java.util.LinkedList;


/**
 * Created by Paweł Kopeć on 22.04.17.
 */
class ClientConnectionManagerTest {
    /*@Test
    void handleClientTest() throws IOException {
        Game exampleGame = new Game() {
            @Override
            public void performAction(Action action, int playerId) {}

            @Override
            public void addPlayer(AccessPoint accessPoint) {}

            @Override
            public void removePlayer(int playerId) {}

            @Override
            public Action parseAction(byte[] args) {return null;}

            @Override
            public boolean isActive() {return false;}

            @Override
            public void makeTurn() {}

            @Override
            public void sendUpdates() {}
        };

        LinkedList<Integer> ports = new LinkedList<>();
        ports.add(9091);
        ports.add(9092);
        PortManager portManager = new PortManager(ports, 2);

        InetAddress localhost = InetAddress.getByName("localhost");

        ClientConnectionManager manager =
                new ClientConnectionManager(exampleGame, portManager, 9090);

        byte[] data = new byte[4];

        Socket clientSocket = new Socket(localhost, 9093);
        DatagramSocket clientUDPSocket = new DatagramSocket(9094, localhost);
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, localhost, );

        clientSocket.getOutputStream().write();
    }*/
}