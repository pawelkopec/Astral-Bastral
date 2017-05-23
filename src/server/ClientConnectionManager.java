package server;

import game.Game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

    private static final int CLIENT_RESPONSE_TIMEOUT = 1000000;

    private static final String GAME_NULL = "Game cannot be null";
    private static final String PORT_MANAGER_NULL = "Port manager cannot be null";

    private static final String LISTENING_STARTED = "Started listening on port %d";
    private static final String NEW_CONNECTION = "Accepted new connection with %s on port %d";
    private static final String NEW_PLAYER = "New player added to with id %d";
    private static final String NO_PORTS_AVAILABLE = "No ports available for a new player";
    private static final String ADDING_NEW_PLAYER_FAILED = "Adding new player from %s";

    private Game game;
    private ServerSocket listeningSocket;

    private PortManager portManager;
    private ExecutorService executor;
    private Logger logger;

    private boolean working;

    public ClientConnectionManager(Game game, PortManager portManager, int listeningPort, Logger logger) throws IOException {
        if (game == null) {
            throw new NullPointerException(GAME_NULL);
        }

        if (portManager == null) {
            throw new NullPointerException(PORT_MANAGER_NULL);
        }

        if (!Ports.isValidListeningPortNumber(listeningPort)) {
            throw new IllegalArgumentException(Ports.INVALID_PORT_NUMBER);
        }

        if (logger == null) {
            this.logger = Logger.getDefaultLogger();
        } else {
            this.logger = logger;
        }
        this.logger.initialize();

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

        //TODO managing loop
        working = true;
        Socket socket = null;

        logger.accept(String.format(LISTENING_STARTED, listeningSocket.getLocalPort()));

        while (working) {
            try {
                socket = listeningSocket.accept();
                logger.accept(String.format(NEW_CONNECTION, socket.getInetAddress(), socket.getPort()));
                executor.submit(new HandleClient(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //TODO closing all resources in another method
        try {
            socket.close();
            logger.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class HandleClient implements Runnable {

        private Socket socket;

        HandleClient(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            UDPAccessPoint clientAccessPoint = null;
            DataOutputStream clientOutputStream = null;
            DataInputStream clientInputStream = null;
            int localPort, clientPort;
            try {
                localPort = portManager.getAvailablePort();
                clientOutputStream = new DataOutputStream(socket.getOutputStream());
                clientInputStream = new DataInputStream(socket.getInputStream());

                clientOutputStream.writeInt(localPort);

                if (localPort == NO_PORT) {
                    return;
                }

                clientPort = clientInputStream.readInt();

                clientAccessPoint = newAccessPoint(clientPort, localPort, socket.getInetAddress());
                int playerId = game.addPlayer(clientAccessPoint);

                if (playerId != Game.FAILURE) {
                    logger.accept(String.format(NEW_PLAYER, playerId));
                } else {
                    logger.accept(String.format(ADDING_NEW_PLAYER_FAILED, socket.getInetAddress()));
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (clientAccessPoint != null) {
                        clientAccessPoint.close();
                    }

                    if (clientInputStream != null) {
                        clientInputStream.close();
                    }

                    if (clientOutputStream != null) {
                        clientOutputStream.close();
                    }

                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
    private UDPAccessPoint newAccessPoint(int peerPort, int portIn, InetAddress address) {
        UDPAccessPoint newAccessPoint = null;

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
