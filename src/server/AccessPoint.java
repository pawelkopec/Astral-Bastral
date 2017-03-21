package server;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * A class that provides a two-way connection
 * with another host;
 */
public interface AccessPoint {

    void sendData(byte[] data);

    byte[] getData();
}
