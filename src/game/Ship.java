package game;

/**
 * Created by micha on 22.04.2017.
 *
 * Base class for ships used in the game.
 */
public abstract class Ship extends GameEntity {

    // Speed of the ship.
    protected float speed;


    public Ship(
            GameEntitiesTypes type,
            float x, float y, float rotation,
            int hp, int collisionDmg, float speed
    ) {
        super(type, x, y, rotation, hp, collisionDmg);
        this.speed = speed;
    }

    @Override
    public void move() {
        x += speed * dx;
        y += speed * dy;
    }

}
