package server;

import game.Game;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.*;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * A runnable that waits for new players and
 * and adds them to game.
 */
public class ClientConnectionManager implements Runnable {

    private static final int NO_PORT = -1;
    private static final Integer [] DEFAULT_PORTS = {5000, 5001};

    private Game game;
    private LinkedList<Integer> availablePorts;

    public ClientConnectionManager(Game game, Collection<Integer> ports) {
        this.game = game;
        this.availablePorts = new LinkedList<>();
        for (Integer port : ports) {
            if (Ports.isValidPortNumber(port)) {
                availablePorts.add(port);
            }
        }
    }

    public ClientConnectionManager(Game game, Integer [] ports) {
        this(game, new ArrayList<Integer>(Arrays.asList(ports)));
    }

    public ClientConnectionManager(Game game) {
        this(game, DEFAULT_PORTS);
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
        int portIn, portOut;
        while (true) {
            try {
                portIn = popPort();
                portOut = popPort();
                if (portIn != NO_PORT && portOut != NO_PORT) {
                    accessPoint = new UDPAccessPoint(portIn, portOut, peerPort, address);
                }
                else {
                    pushPort(portIn);
                    pushPort(portOut);
                }

                break;
            } catch (SocketException ignored) {}
        }

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
        while (true) {
            try {
                newAccessPoint = new UDPAccessPoint(popPort(), popPort(), peerPort, address);
                break;
            } catch (SocketException ignored) {}
        }

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
        pushPort(accessPoint.getPortIn());
        pushPort(accessPoint.getPortOut());
        accessPoint.close();
    }

    private int popPort() {
        //TODO
        return 0;
    }

    private void pushPort(int port) {
        //TODO
    }
}
