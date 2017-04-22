package game;

/**
 * Created by micha on 22.04.2017.
 */
public class EnemyMissile extends Missile {

    public EnemyMissile(
            MissilesTypes missileType,
            float x, float y, float rotation
    ) {
        super(GameEntitiesTypes.ENEMY_MISSILE, missileType, x, y, rotation);
    }

    public void move() {

    }

    public boolean[] getCollisionWhiteList(){
        return null;
    }

    public void collide(GameEntity entity){

    }

}
