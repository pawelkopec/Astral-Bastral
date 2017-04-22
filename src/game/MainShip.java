package game;

/**
 * Created by micha on 22.04.2017.
 */
public class MainShip extends Ship {

    private static final int MAIN_SHIP_HP = 10000;
    private static final int MAIN_SHIP_DMG = 50;
    private static final float MAIN_SHIP_SPEED = (float) 0.0;

    public MainShip(float x, float y, float rotation) {
        super(
            GameEntitiesTypes.MAIN_SHIP,
            x, y, rotation,
            MAIN_SHIP_HP, MAIN_SHIP_DMG, MAIN_SHIP_SPEED
        );
    }

    public boolean[] getCollisionWhiteList(){
        return null;
    }

    public void collide(GameEntity entity){

    }

}
