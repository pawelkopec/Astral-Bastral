package game;

/**
 * Created by micha on 22.04.2017.
 */
public class Asteroid extends GameEntity {

    private static final int ASTEROID_HP = 15;
    private static final int ASTEROID_DMG = 10;
    private static final float ASTEROID_SPEED = (float) 5.0;

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


    public Asteroid(float x, float y, float rotation) {
        super(
            GameEntitiesTypes.ASTEROID,
            x, y, rotation,
            ASTEROID_HP, ASTEROID_DMG
        );
    }

    public void move() {
        x += ASTEROID_SPEED * dx;
        y += ASTEROID_SPEED * dy;
    }

    public boolean[] getCollisionWhiteList(){
        return collisionWhiteList;
    }

    public void collide(GameEntity entity){

    }

}
