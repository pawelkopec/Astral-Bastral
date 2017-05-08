package game;

/**
 * Created by micha on 22.04.2017.
 *
 * Class for player controlled turrets.
 */
public class Turret extends GameEntity {

    // Turrets constants.
    private static final int TURRET_HP = 0;
    private static final int TURRET_DMG = 0;

    // Collision white list specific to turrets.
    private static boolean[] collisionWhiteList;

    static {

        // Turrets don't collide with anything.
        collisionWhiteList = new boolean[GameEntitiesTypes.values().length];
        collisionWhiteList[GameEntitiesTypes.MAIN_SHIP.getValue()] = false;
        collisionWhiteList[GameEntitiesTypes.TURRET.getValue()] = false;
        collisionWhiteList[
        GameEntitiesTypes.FRIENDLY_MISSILE.getValue()
        ] = false;
        collisionWhiteList[GameEntitiesTypes.ENEMY_MISSILE.getValue()] = false;
        collisionWhiteList[GameEntitiesTypes.ENEMY_SHIP.getValue()] = false;
        collisionWhiteList[GameEntitiesTypes.ASTEROID.getValue()] = false;

    }


    // Id needed to connect turret to player.
    private int playerId;


    public Turret(int playerId, float x, float y, float rotation) {
        super(GameEntitiesTypes.TURRET, x, y, rotation, TURRET_HP, TURRET_DMG);
        this.playerId = playerId;
    }

    // Method used to rotate turret.
    public void rotate(float rotation) {
        this.rotation = rotation;
    }

    public boolean[] getCollisionWhiteList(){
        return collisionWhiteList;
    }

    // Might be not needed.
    public int getPlayerId() {
        return playerId;
    }

}
