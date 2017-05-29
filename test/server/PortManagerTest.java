package server;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by vivace on 22.04.17.
 *
 * Tests for PortManager
 */
class PortManagerTest {
    @Test
    void getAvailablePort() {

        ArrayList<Integer> ports = new ArrayList<>();
        ports.add(3004);
        ports.add(5034);
        ports.add(23098);
        ports.add(4221);

        PortManager portManager = new PortManager(ports);

        for(int i = 0 ; i < ports.size(); i++) {
            assertTrue(ports.contains(portManager.getAvailablePort()));
        }

        assertEquals(portManager.getAvailablePort(), Ports.NO_PORT);
    }

    @Test
    void freePort() {
        ArrayList<Integer> ports = new ArrayList<>();
        ports.add(4304);
        ports.add(2434);
        ports.add(2398);
        ports.add(26751);

        PortManager portManager = new PortManager(ports);

        for(int i = 0 ; i < ports.size(); i++) {
            portManager.getAvailablePort();
        }

        portManager.freePort(ports.get(0));
        assertEquals(portManager.getAvailablePort(), ports.get(0).intValue());
    }

}