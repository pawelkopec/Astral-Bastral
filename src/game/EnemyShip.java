package game;

/**
 * Created by micha on 22.04.2017.
 */
public class EnemyShip extends Ship {

    private static final int ENEMY_SHIP_HP = 50;
    private static final int ENEMY_SHIP_DMG = 10;
    private static final float ENEMY_SHIP_SPEED = (float) 0.0;

    private static boolean[] collisionWhiteList;

    static {
        collisionWhiteList = new boolean[GameEntitiesTypes.values().length];
        collisionWhiteList[GameEntitiesTypes.MAIN_SHIP.getValue()] = true;
        collisionWhiteList[GameEntitiesTypes.TURRET.getValue()] = false;
        collisionWhiteList[
        GameEntitiesTypes.FRIENDLY_MISSILE.getValue()
        ] = true;
        collisionWhiteList[GameEntitiesTypes.ENEMY_MISSILE.getValue()] = true;
        collisionWhiteList[GameEntitiesTypes.ENEMY_SHIP.getValue()] = true;
        collisionWhiteList[GameEntitiesTypes.ASTEROID.getValue()] = true;
    }


    public EnemyShip(float x, float y, float rotation) {
        super(
            GameEntitiesTypes.ENEMY_SHIP,
            x, y, rotation,
            ENEMY_SHIP_HP, ENEMY_SHIP_DMG, ENEMY_SHIP_SPEED
        );
    }

    public boolean[] getCollisionWhiteList(){
        return collisionWhiteList;
    }

}
