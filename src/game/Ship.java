package game;

/**
 * Created by micha on 22.04.2017.
 */
public abstract class Ship extends GameEntity {

    protected float speed;


    public Ship(
            GameEntitiesTypes type,
            float x, float y, float rotation,
            int hp, int collisionDmg, float speed
    ) {
        super(type, x, y, rotation, hp, collisionDmg);
        this.speed = speed;
    }

    public void move() {
        x += speed * dx;
        y += speed * dy;
    }

}
