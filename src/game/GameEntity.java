package game;

/**
 * Created by micha on 22.04.2017.
 */
public abstract class GameEntity {

    // Temporary constant collision range for entities.
    private static final float COLLISION_RANGE = (float) 20.0;


    protected GameEntitiesTypes type;
    protected boolean active;

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
        this.type = type;
        this.active = true;
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.dx = (float) Math.cos(rotation);
        this.dy = (float) Math.sin(rotation);
        this.hp = hp;
        this.collisionDmg = collisionDmg;
    }

    public GameEntitiesTypes getType() {
        return type;
    }

    public boolean isActive() {
        return active;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void receiveDamage(int damage) {
        hp -= damage;
        if (hp <= 0) {
           active = false;
        }
    }

    // Abstract methods which define how particular entity moves and
    // how it behaves when collision occurs.
    public abstract void move();
    public abstract boolean[] getCollisionWhiteList();

    public void collide(GameEntity entity) {
        entity.receiveDamage(collisionDmg);
    }

    public boolean isCollidingWith(GameEntity entity) {
        return Math.abs(entity.getX() - x) +
               Math.abs((entity.getY() - y)) <= COLLISION_RANGE &&
               (active && entity.isActive());
    }

}
