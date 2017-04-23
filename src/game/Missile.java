package game;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by puchake on 21.03.17.
 *
 * Class describing in-game missile.
 */
public abstract class Missile extends GameEntity {

    // Temporary constant for missile hp, dmg and speed.
    private static final int MISSILE_HP = 1;
    private static final int MISSILE_DMG = 10;
    private static final float MISSILE_SPEED = (float) 5.0;


    protected MissilesTypes missileType;


    public Missile(
            GameEntitiesTypes type, MissilesTypes missileType,
            float x, float y, float rotation
    ) {
        super(type, x, y, rotation, MISSILE_HP, MISSILE_DMG);
        this.missileType = missileType;
    }

    @Override
    public void move() {
        x += MISSILE_SPEED * dx;
        y += MISSILE_SPEED * dy;
    }

    @Override
    public void writeTo(DataOutputStream output) throws IOException {
        output.writeShort(type.getValue());

        // Missiles need to also output their missile type.
        output.writeShort(missileType.getValue());

        output.writeFloat(x);
        output.writeFloat(y);
        output.writeFloat(rotation);
    }

}
