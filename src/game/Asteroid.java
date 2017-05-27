package game;

/**
 * Created by micha on 22.04.2017.
 *
 * Class which represents in-game asteroid.
 */
public class Asteroid extends GameEntity {

    // Constant for asteroid.
    private static final int ASTEROID_HP = 15;
    private static final int ASTEROID_DMG = 10;
    private static final float ASTEROID_SPEED = (float) 5.0;

    // Static collision white list common to all asteroids.
    private static boolean[] collisionWhiteList;

    static {

        // Asteroids can collide with everything except for turrets.
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


    public Asteroid(float x, float y, float rotation) {
        super(
            GameEntitiesTypes.ASTEROID,
            x, y, rotation,
            ASTEROID_HP, ASTEROID_DMG
        );
    }

    @Override
    public void move(float deltaTime) {
        x += ASTEROID_SPEED * dx * deltaTime;
        y += ASTEROID_SPEED * dy * deltaTime;
    }

    public boolean[] getCollisionWhiteList(){
        return collisionWhiteList;
    }

}
