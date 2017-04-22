package game;

/**
 * Created by puchake on 21.03.17.
 *
 * Class describing in-game missile.
 */
public abstract class Missile extends GameEntity {

    // Temporary constant for missile hp and dmg.
    private static final int MISSILE_HP = 1;
    private static final int MISSILE_DMG = 10;


    protected MissilesTypes missileType;


    public Missile(
            GameEntitiesTypes type, MissilesTypes missileType,
            float x, float y, float rotation
    ) {
        super(type, x, y, rotation, MISSILE_HP, MISSILE_DMG);
        this.missileType = missileType;
    }

}
