package server;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * Implementation of two-way connection
 * implemented with UDP protocol.
 */
public class UDPAccessPoint implements AccessPoint {

    @Override
    public void sendData(byte[] data) {

    }

    @Override
    public byte[] getData() {
        return new byte[0];
    }
}
