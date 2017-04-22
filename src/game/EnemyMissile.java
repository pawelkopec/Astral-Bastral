package game;

/**
 * Created by micha on 22.04.2017.
 */
public class EnemyMissile extends Missile {

    private static boolean[] collisionWhiteList;

    static {
        collisionWhiteList = new boolean[GameEntitiesTypes.values().length];
        collisionWhiteList[GameEntitiesTypes.MAIN_SHIP.getValue()] = true;
        collisionWhiteList[GameEntitiesTypes.TURRET.getValue()] = false;
        collisionWhiteList[
        GameEntitiesTypes.FRIENDLY_MISSILE.getValue()
        ] = true;
        collisionWhiteList[GameEntitiesTypes.ENEMY_MISSILE.getValue()] = false;
        collisionWhiteList[GameEntitiesTypes.ENEMY_SHIP.getValue()] = false;
        collisionWhiteList[GameEntitiesTypes.ASTEROID.getValue()] = true;
    }


    public EnemyMissile(
            MissilesTypes missileType,
            float x, float y, float rotation
    ) {
        super(GameEntitiesTypes.ENEMY_MISSILE, missileType, x, y, rotation);
    }

    public boolean[] getCollisionWhiteList(){
        return collisionWhiteList;
    }

}
