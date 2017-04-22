package game;

/**
 * Created by micha on 22.04.2017.
 */
public abstract class GameEntity {

    protected GameEntitiesTypes type;

    // Position fields.
    protected float x, y;
    protected float rotation;

    // Movement vector coordinates.
    protected float dx, dy;

    // Hit points and damage applied to other entity on collision.
    protected int hp;
    protected int collisionDmg;


    public GameEntity(
            GameEntitiesTypes type,
            float x, float y, float rotation,
            int hp, int collisionDmg
    ) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.dx = (float) Math.cos(rotation);
        this.dy = (float) Math.sin(rotation);
        this.hp = hp;
        this.collisionDmg = collisionDmg;
    }

    // Abstract methods which define how particular entity moves and
    // how it behaves when collision occurs.
    public abstract void move();
    public abstract boolean[] getCollisionWhiteList();
    public abstract void collide(GameEntity entity);

}
