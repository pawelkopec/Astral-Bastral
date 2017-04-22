package game;

/**
 * Created by micha on 22.04.2017.
 */
public class Turret extends GameEntity {

    private static final int TURRET_HP = 0;
    private static final int TURRENT_DMG = 0;

    private static boolean[] collisionWhiteList;

    static {
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


    public Turret(float x, float y, float rotation) {
        super(GameEntitiesTypes.TURRET, x, y, rotation, TURRET_HP, TURRENT_DMG);
    }

    public void move() {

    }

    public boolean[] getCollisionWhiteList(){
        return collisionWhiteList;
    }

    public void collide(GameEntity entity){

    }

}
