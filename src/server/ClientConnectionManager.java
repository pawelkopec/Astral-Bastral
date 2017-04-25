package server;

import game.Game;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static server.Ports.NO_PORT;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * A runnable that waits for new players and
 * and adds them to game.
 */
public class ClientConnectionManager implements Runnable {

    private static final int CLIENT_RESPONSE_TIMEOUT = 100;

    private static final String GAME_NULL = "Game cannot be null";
    private static final String PORT_MANAGER_NULL = "Port manager cannot be null";

    private Game game;
    private ServerSocket listeningSocket;

    private PortManager portManager;
    private ExecutorService executor;

    private boolean working;

    public ClientConnectionManager(Game game, PortManager portManager, int listeningPort) throws IOException {
        if (game == null) {
            throw new NullPointerException(GAME_NULL);
        }

        if (portManager == null) {
            throw new NullPointerException(PORT_MANAGER_NULL);
        }

        if (!Ports.isValidListeningPortNumber(listeningPort)) {
            throw new IllegalArgumentException(Ports.INVALID_PORT_NUMBER);
        }

        this.game = game;
        this.portManager = portManager;

        listeningSocket = new ServerSocket(listeningPort);
        //TODO
        executor = new ThreadPoolExecutor(1, 1,
                CLIENT_RESPONSE_TIMEOUT,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    @Override
    public void run() {

        working = true;

        int localPort;
        Socket socket;

        while (working) {
            try {
                socket = listeningSocket.accept();
                localPort = portManager.getAvailablePort();
                socket.getOutputStream().write(localPort);

                if (localPort != NO_PORT) {
                    executor.submit(new HandleClient(socket));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class HandleClient implements Runnable {

        private Socket socket;

        HandleClient(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                int clientPort = socket.getInputStream().read();
                game.addPlayer(newAccessPoint(clientPort, socket.getInetAddress()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public void destroyAccessPoint(UDPAccessPoint accessPoint) {
        portManager.freePort(accessPoint.getPortIn());
        portManager.freePort(accessPoint.getPortOut());
        accessPoint.close();
    }

    private boolean portsFine(int portIn, int portOut) {
        return portIn != NO_PORT && portOut != NO_PORT;
    }

    public void stop() throws IOException {
        working = false;
        listeningSocket.close();
        //TODO
    }
}
