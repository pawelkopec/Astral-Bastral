package game;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by micha on 22.04.2017.
 *
 * Class which represents in-game asteroid.
 */
public class Asteroid extends GameEntity {

    // Constant for asteroid.
    private static final int ASTEROID_HP = 15;
    private static final int ASTEROID_DMG = 10;
    private static final float ASTEROID_SPEED = (float) 25.0;
    private static final float ASTEROID_ROTATION_SPEED = (float) 0.5;

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


    // Asteroids have their own inner rotation except for movement rotation.
    private float innerRotation;


    public Asteroid(float x, float y, float rotation) {
        super(
            GameEntitiesTypes.ASTEROID,
            x, y, rotation,
            ASTEROID_HP, ASTEROID_DMG
        );
        innerRotation = 0.0f;
    }

    @Override
    public void move(float deltaTime) {
        x += ASTEROID_SPEED * dx * deltaTime;
        y += ASTEROID_SPEED * dy * deltaTime;
        // Slowly rotate asteroid as time passes.
        innerRotation += ASTEROID_ROTATION_SPEED * deltaTime;
        if (innerRotation < 0.0f) {
            innerRotation += Math.PI * 2;
        }
        if (innerRotation > Math.PI * 2) {
            innerRotation -= Math.PI * 2;
        }
    }

    public boolean[] getCollisionWhiteList(){
        return collisionWhiteList;
    }

    @Override
    public void writeTo(DataOutputStream output) throws IOException {
        output.writeShort(type.getValue());
        output.writeFloat(innerRotation);
        output.writeFloat(x);
        output.writeFloat(y);
        output.writeFloat(rotation);
    }

}
