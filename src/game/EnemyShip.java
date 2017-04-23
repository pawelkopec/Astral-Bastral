package game;

/**
 * Created by micha on 22.04.2017.
 *
 * Class which represents enemy ships.
 */
public class EnemyShip extends Ship {

    // Enemy ships constants.
    private static final int ENEMY_SHIP_HP = 50;
    private static final int ENEMY_SHIP_DMG = 10;
    private static final float ENEMY_SHIP_SPEED = (float) 0.0;
    private static final float MISSILE_SPAWN_PROBABILITY = (float) 0.05;

    // Collision white list unique to all enemy ships.
    private static boolean[] collisionWhiteList;

    static {

        // Initialization of collision white list. Enemy ships can collide with
        // everything except for turrets and enemy missiles.
        collisionWhiteList = new boolean[GameEntitiesTypes.values().length];
        collisionWhiteList[GameEntitiesTypes.MAIN_SHIP.getValue()] = true;
        collisionWhiteList[GameEntitiesTypes.TURRET.getValue()] = false;
        collisionWhiteList[
        GameEntitiesTypes.FRIENDLY_MISSILE.getValue()
        ] = true;
        collisionWhiteList[GameEntitiesTypes.ENEMY_MISSILE.getValue()] = false;
        collisionWhiteList[GameEntitiesTypes.ENEMY_SHIP.getValue()] = true;
        collisionWhiteList[GameEntitiesTypes.ASTEROID.getValue()] = true;

    }


    public EnemyShip(float x, float y) {
        super(
            GameEntitiesTypes.ENEMY_SHIP,
            x, y, (float) Math.atan2(-y, -x),
            ENEMY_SHIP_HP, ENEMY_SHIP_DMG, ENEMY_SHIP_SPEED
        );
    }

    public boolean[] getCollisionWhiteList(){
        return collisionWhiteList;
    }

    @Override
    public GameEntity act() {
        if (randomGenerator.nextFloat() <= MISSILE_SPAWN_PROBABILITY) {

            // Create new basic missile 2 collision ranges from this ship.
            float missileX = x + dx * 2 * COLLISION_RANGE;
            float missileY = y + dy * 2 * COLLISION_RANGE;
            return new EnemyMissile(
                MissilesTypes.BASIC_MISSILE, missileX, missileY, rotation
            );

        }
        return null;
    }

}
