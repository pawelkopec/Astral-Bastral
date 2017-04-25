package server;

import org.junit.jupiter.api.Test;
import testutil.ThreadTester;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by Paweł Kopeć on 22.04.17.
 */
class UDPAccessPointTest {

    private static final int PORT_IN = 9090;
    private static final int PORT_OUT = 9091;
    private static final int PORT_LISTEN = 9092;
    private static final int PORT_SEND = 9093;
    private static final int PORT_PEER = 80;
    private static final String PEER_HOST = "localhost";

    @Test
    void getPortInTest() throws UnknownHostException, SocketException {
        InetAddress peerAddress = InetAddress.getByName(PEER_HOST);
        UDPAccessPoint udpAccessPoint = new UDPAccessPoint(PORT_IN, PORT_OUT, PORT_PEER, peerAddress);

        try {
            assertEquals(udpAccessPoint.getPortIn(), PORT_IN);
        } finally {
            udpAccessPoint.close();
        }
    }

    @Test
    void getPortOutTest() throws UnknownHostException, SocketException {
        InetAddress peerAddress = InetAddress.getByName(PEER_HOST);
        UDPAccessPoint udpAccessPoint = new UDPAccessPoint(PORT_IN, PORT_OUT, PORT_PEER, peerAddress);

        try {
            assertEquals(udpAccessPoint.getPortOut(), PORT_OUT);
        } finally {
            udpAccessPoint.close();
        }
    }

    @Test
    void getPortPeerTest() throws UnknownHostException, SocketException {
        InetAddress peerAddress = InetAddress.getByName(PEER_HOST);
        UDPAccessPoint udpAccessPoint = new UDPAccessPoint(PORT_IN, PORT_OUT, PORT_PEER, peerAddress);

        try {
            assertEquals(udpAccessPoint.getPeerPort(), PORT_PEER);
        } finally {
            udpAccessPoint.close();
        }
    }

    @Test
    void getPeerAddressTest() throws UnknownHostException, SocketException {
        InetAddress peerAddress = InetAddress.getByName(PEER_HOST);
        UDPAccessPoint udpAccessPoint = new UDPAccessPoint(PORT_IN, PORT_OUT, PORT_PEER, peerAddress);

        try {
            assertEquals(udpAccessPoint.getPeerAddress(), peerAddress);
        } finally {
            udpAccessPoint.close();
        }
    }

    @Test
    void sendDataTest() throws IOException, InterruptedException {
        InetAddress localhost = InetAddress.getByName("localhost");
        UDPAccessPoint udpAccessPoint = new UDPAccessPoint(PORT_IN, PORT_OUT, PORT_LISTEN, localhost);

        byte[] data = new byte[]{3, 4, 5, 6};
        new DatagramPacket(data, data.length, localhost, udpAccessPoint.getPortOut());

        byte[] buff = new byte[4];
        DatagramPacket receivedPacket = new DatagramPacket(buff, buff.length);

        DatagramSocket listeningSocket = new DatagramSocket(PORT_LISTEN, localhost);


        ThreadTester threadTester = new ThreadTester(() -> {
            try {
                listeningSocket.receive(receivedPacket);
            } catch (IOException e) {
                e.printStackTrace();
                udpAccessPoint.close();
                listeningSocket.close();
            }

            assertTrue(Arrays.equals(data, buff));
            assertEquals(receivedPacket.getPort(), udpAccessPoint.getPortOut());
        });

        threadTester.startThread();
        udpAccessPoint.sendData(data);

        threadTester.checkAssertion();
        udpAccessPoint.close();
        listeningSocket.close();
    }

    @Test
    void receiveDataTest() throws IOException, InterruptedException {
        InetAddress localhost = InetAddress.getByName("localhost");
        UDPAccessPoint udpAccessPoint = new UDPAccessPoint(PORT_IN, PORT_OUT,
                PORT_PEER, localhost);

        byte[] data = new byte[]{3, 4, 5, 6};
        DatagramPacket sendPacket = new DatagramPacket(data, data.length,
                udpAccessPoint.getPeerAddress(), udpAccessPoint.getPortIn());

        DatagramSocket sendingSocket = new DatagramSocket(PORT_SEND, localhost);


        ThreadTester threadTester = new ThreadTester(() -> {
            byte[] received = null;

            try {
                received = udpAccessPoint.getData();
            } catch (IOException e) {
                e.printStackTrace();
                udpAccessPoint.close();
                sendingSocket.close();
            }
            assertTrue(Arrays.equals(data, received));
        });

        threadTester.startThread();
        sendingSocket.send(sendPacket);

        threadTester.checkAssertion();
        udpAccessPoint.close();
        sendingSocket.close();
    }
}