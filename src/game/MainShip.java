package game;

/**
 * Created by micha on 22.04.2017.
 */
public class MainShip extends Ship {

    private static final int MAIN_SHIP_HP = 10000;
    private static final int MAIN_SHIP_DMG = 50;
    private static final float MAIN_SHIP_SPEED = (float) 0.0;

    private static boolean[] collisionWhiteList;

    static {
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

    public void collide(GameEntity entity){

    }

}
