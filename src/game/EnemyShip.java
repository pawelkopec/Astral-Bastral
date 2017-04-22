package game;

/**
 * Created by micha on 22.04.2017.
 */
public class EnemyShip extends Ship {

    private static final int ENEMY_SHIP_HP = 10000;
    private static final int ENEMY_SHIP_DMG = 50;
    private static final float ENEMY_SHIP_SPEED = (float) 0.0;

    public EnemyShip(float x, float y, float rotation) {
        super(
            GameEntitiesTypes.ENEMY_SHIP,
            x, y, rotation,
            ENEMY_SHIP_HP, ENEMY_SHIP_DMG, ENEMY_SHIP_SPEED
        );
    }

    public boolean[] getCollisionWhiteList(){
        return null;
    }

    public void collide(GameEntity entity){

    }

}
