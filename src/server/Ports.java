package server;

/**
 * Created by vivace on 23.03.17.
 *
 * Helpful class for port management.
 */
public class Ports {

    static final int MIN_PORT_NUM = 2000, MAX_PORT_NUM = 65535;

    static final String INVALID_PORT_NUMBER = "Port number must have a value between 0 and " + MAX_PORT_NUM + ".";

    static boolean isValidPortNumber(int port) {
        return MIN_PORT_NUM <= port && port <= MAX_PORT_NUM;
    }
}
