package game;

/**
 * Created by micha on 22.04.2017.
 *
 * Definition of class which represents main game ship.
 */
public class MainShip extends Ship {

    // Main ship constants: its hp, collision dmg and speed.
    private static final int MAIN_SHIP_HP = 5000;
    private static final int MAIN_SHIP_DMG = 50;
    private static final float MAIN_SHIP_SPEED = (float) 0.0;

    // Private collision white list unique to main ship.
    private static boolean[] collisionWhiteList;

    static {

        // Collision white list initialization for this class. Main ship can
        // collide with everything except for turrets, friendly missiles and
        // itself.
        collisionWhiteList = new boolean[GameEntitiesTypes.values().length];
        collisionWhiteList[GameEntitiesTypes.MAIN_SHIP.getValue()] = false;
        collisionWhiteList[GameEntitiesTypes.TURRET.getValue()] = false;
        collisionWhiteList[
        GameEntitiesTypes.FRIENDLY_MISSILE.getValue()
        ] = false;
        collisionWhiteList[GameEntitiesTypes.ENEMY_MISSILE.getValue()] = true;
        collisionWhiteList[GameEntitiesTypes.ENEMY_SHIP.getValue()] = true;
        collisionWhiteList[GameEntitiesTypes.ASTEROID.getValue()] = true;

    }


    public MainShip(float x, float y, float rotation) {
        super(
            GameEntitiesTypes.MAIN_SHIP,
            x, y, rotation,
            MAIN_SHIP_HP, MAIN_SHIP_DMG, MAIN_SHIP_SPEED
        );
    }

    public boolean[] getCollisionWhiteList(){
        return collisionWhiteList;
    }

    // Access to man ship hp.
    public int getHp() {
        return this.hp;
    }

}
