package server;

import game.Game;

import java.net.InetAddress;
import java.net.SocketException;

import static server.Ports.NO_PORT;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * A runnable that waits for new players and
 * and adds them to game.
 */
public class ClientConnectionManager implements Runnable {

    private static final String GAME_NULL = "Game cannot be null";
    private static final String PORT_MANAGER_NULL = "Port manager cannot be null";

    private Game game;

    private PortManager portManager;

    public ClientConnectionManager(Game game, PortManager portManager) {
        if (game == null) {
            throw new NullPointerException(GAME_NULL);
        }

        if (portManager == null) {
            throw new NullPointerException(PORT_MANAGER_NULL);
        }

        this.game = game;
        this.portManager = portManager;
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
        UDPAccessPoint accessPoint = newAccessPoint(peerPort, address);

        //TODO creating and activating new client
    }

    /**
     * Create new AccessPoint that handles the same client.
     *
     * @param peerPort of a client
     * @param address of a client
     * @return new AccessPoint
     */
    private UDPAccessPoint newAccessPoint(int peerPort, InetAddress address) {
        UDPAccessPoint newAccessPoint = null;

        int portIn = portManager.getAvailablePort();
        int portOut = portManager.getAvailablePort();

        if (!portsFine(portIn, portOut)) {
            return null;
        }

        try {
            newAccessPoint = new UDPAccessPoint(portIn, portOut, peerPort, address);
        } catch (SocketException ignored) {}

        return newAccessPoint;
    }

    /**
     * Create new AccessPoint that handles the same client.
     *
     * @param accessPoint old AccessPOint
     * @return new AccessPoint
     */
    public UDPAccessPoint newAccessPoint(UDPAccessPoint accessPoint) {
        return newAccessPoint(accessPoint.getPeerPort(), accessPoint.getPeerAddress());
    }

    public void destroyAccessPoint(UDPAccessPoint accessPoint) {
        portManager.freePort(accessPoint.getPortIn());
        portManager.freePort(accessPoint.getPortOut());
        accessPoint.close();
    }

    private boolean portsFine(int portIn, int portOut) {
        return portIn != NO_PORT && portOut != NO_PORT;
    }
}
