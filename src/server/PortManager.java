package server;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by vivace on 22.04.17.
 *
 * Class for providing and managing a given
 * collection of ports.
 */
class PortManager {

    private static final String PORTS_NULL = "Collection of ports cannot be null.";

    private HashMap<Integer, Boolean> portsAvailability;
    private LinkedList<Integer> availablePorts = new LinkedList<>();

    PortManager(Collection<Integer> ports) {
        if (ports == null) {
            throw new NullPointerException(PORTS_NULL);
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

    synchronized int getAvailablePort() {
        if (availablePorts.isEmpty()) {
            return Ports.NO_PORT;
        }

        int port = availablePorts.pollFirst();
        portsAvailability.put(port, false);

        return port;
    }

    synchronized void freePort(int port) {
        if (portsAvailability.containsKey(port)) {
            availablePorts.addLast(port);
            portsAvailability.put(port, true);
        }
    }
}
