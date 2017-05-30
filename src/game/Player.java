package game;

import server.AccessPoint;

import java.io.IOException;


/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * A class for player in a game.
 */
public class Player {

    // Player's id and index of his turret in game entities array.
    private int playerId;
    private int turretIndex;

    // Player's access point used to transfer data to remote client.
    private AccessPoint accessPoint;


    public Player(int playerId, int turretIndex, AccessPoint accessPoint) {
        this.playerId = playerId;
        this.turretIndex = turretIndex;
        this.accessPoint = accessPoint;
    }

    public int getPlayerId() {
        return  playerId;
    }

    public int getTurretIndex() {
        return turretIndex;
    }

    // Method used to send data to player through his access point.
    public void sendUpdate(byte[] update) throws IOException {
        accessPoint.sendData(update);
    }

    AccessPoint getAccessPoint() {
        return accessPoint;
    }

}
