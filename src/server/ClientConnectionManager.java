package server;

import game.Game;

import java.net.InetAddress;
import java.net.SocketException;

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

    /**
     * Create new player connection based on
     * client's port and address and add it to game.
     *
     * @param peerPort of a new client
     * @param address of a new client
     */
    private void establishConnection(int peerPort, InetAddress address) {
        UDPAccessPoint accessPoint = null;
        while (true) {
            try {
                accessPoint = new UDPAccessPoint(getNewPortIn(), getNewPortOut(), peerPort, address);
                break;
            } catch (SocketException ignored) {}
        }

        //TODO creating and activating new client
    }

    /**
     * Create new AccessPoint that handles the same client.
     *
     * @param accessPoint broken AccessPoint
     * @return new AccessPoint
     */
    public UDPAccessPoint newAccessPoint(UDPAccessPoint accessPoint) {
        UDPAccessPoint newAccessPoint = null;
        while (true) {
            try {
                newAccessPoint = new UDPAccessPoint(getNewPortIn(), getNewPortOut(),
                        accessPoint.getPeerPort(),
                        accessPoint.getPeerAddress());
                break;
            } catch (SocketException ignored) {}
        }

        return newAccessPoint;
    }

    private int getNewPortIn() {
        //TODO available port management
        return 0;
    }

    private int getNewPortOut() {
        //TODO available port management
        return 0;
    }
}
