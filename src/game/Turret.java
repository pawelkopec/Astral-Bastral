package game;

/**
 * Created by micha on 22.04.2017.
 */
public class Turret extends GameEntity {

    private static final int TURRET_HP = 0;
    private static final int TURRENT_DMG = 0;


    public Turret(float x, float y, float rotation) {
        super(GameEntitiesTypes.TURRET, x, y, rotation, TURRET_HP, TURRENT_DMG);
    }

    public void move() {

    }

    public boolean[] getCollisionWhiteList(){
        return null;
    }

    public void collide(GameEntity entity){

    }

}
