package game;

/**
 * Created by micha on 22.04.2017.
 */
public class Asteroid extends GameEntity {

    private static final int ASTEROID_HP = 15;
    private static final int ASTEROID_DMG = 10;


    public Asteroid(float x, float y, float rotation) {
        super(
            GameEntitiesTypes.ASTEROID,
            x, y, rotation,
            ASTEROID_HP, ASTEROID_DMG
        );
    }

    public void move() {

    }

    public boolean[] getCollisionWhiteList(){
        return null;
    }

    public void collide(GameEntity entity){

    }

}
