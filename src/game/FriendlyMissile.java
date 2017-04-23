package game;

/**
 * Created by micha on 22.04.2017.
 *
 * Class for all in-game missiles fired by player's turrets.
 */
public class FriendlyMissile extends Missile {

    // Collision white list for friendly missiles.
    private static boolean[] collisionWhiteList;

    static {

        // Missiles fired by turrets can collide with enemy ships and missiles
        // and asteroids.
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


    public FriendlyMissile(
        MissilesTypes missileType,
        float x, float y, float rotation
    ) {
        super(GameEntitiesTypes.FRIENDLY_MISSILE, missileType, x, y, rotation);
    }

    public boolean[] getCollisionWhiteList(){
        return collisionWhiteList;
    }

}
