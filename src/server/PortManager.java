package server;

import java.util.*;

/**
 * Created by vivace on 22.04.17.
 *
 * Class for providing and managing a given
 * collection of ports.
 */
public class PortManager {

    private static final String PORTS_NULL = "Collection of ports cannot be null.";
    private static final String INVALID_MAX_CONNECTIONS = "Max connections must be between 1 and %i";

    private HashMap<Integer, Boolean> portsAvailability;

    private LinkedList<Integer> availablePorts = new LinkedList<>();

    private int maxConnections;

    public PortManager(Collection<Integer> ports, int maxConnections) {
        if (ports == null) {
            throw new NullPointerException(PORTS_NULL);
        }

        if (maxConnections < 1 || ports.size() < maxConnections) {
            throw new IllegalArgumentException(String.format(INVALID_MAX_CONNECTIONS, ports.size()));
        }

        portsAvailability = new HashMap<>(ports.size());

        for (Integer port : ports) {
            if (!Ports.isValidPortNumber(port)) {
                throw new IllegalArgumentException(Ports.INVALID_PORT_NUMBER);
            }

            portsAvailability.put(port, true);
            availablePorts.push(port);
        }
    }

    public int getAvailablePort() {
        if (availablePorts.isEmpty()) {
            return Ports.NO_PORT;
        }

        int port = availablePorts.pollFirst();
        portsAvailability.put(port, false);

        return port;
    }

    public void freePort(int port) {
        if (!portsAvailability.containsKey(port)) {
            throw new IllegalArgumentException();
        }

        availablePorts.addLast(port);
        portsAvailability.put(port, true);
    }
}
