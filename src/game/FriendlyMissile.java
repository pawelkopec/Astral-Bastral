package game;

/**
 * Created by micha on 22.04.2017.
 */
public class FriendlyMissile extends Missile {

    public FriendlyMissile(
        MissilesTypes missileType,
        float x, float y, float rotation
    ) {
        super(GameEntitiesTypes.FRIENDLY_MISSILE, missileType, x, y, rotation);
    }

    public void move() {

    }

    public boolean[] getCollisionWhiteList(){
        return null;
    }

    public void collide(GameEntity entity){

    }

}
