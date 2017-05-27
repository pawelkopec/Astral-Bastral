package game;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;


/**
 * Created by micha on 22.04.2017.
 *
 * Base abstract class for all in-game entities.
 */
public abstract class GameEntity {

    // Temporary constant collision range for entities.
    protected static final float COLLISION_RANGE = (float) 20.0;

    // Random generator used to determine if random events such as missile
    // spawn will happen or not.
    protected static Random randomGenerator = new Random();


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

        // Derive coordinates of movement direction vector from rotation.
        this.dx = (float) Math.cos(rotation - Math.PI / 2);
        this.dy = (float) Math.sin(rotation - Math.PI / 2);

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

    public float getRotation() {
        return rotation;
    }

    public void receiveDamage(int damage) {
        hp -= damage;

        // If entity received more damage than it had hp set its state to
        // inactive.
        if (hp <= 0) {
           active = false;
        }

    }

    // Set of collision methods along with abstract collision white list
    // getter.
    public abstract boolean[] getCollisionWhiteList();

    public void collideWith(GameEntity entity) {
        entity.receiveDamage(collisionDmg);
    }

    public boolean isCollidingWith(GameEntity entity) {
        return Math.abs(entity.getX() - x) +
               Math.abs((entity.getY() - y)) <= COLLISION_RANGE &&
               (active && entity.isActive());
    }

    // Base method used for entity movement.
    public void move(float deltaTime) {

    }

    // Method used for entity action. Result of and action is
    // generally a new GameEntity object (for example spawned missile).
    public GameEntity act() {
        return null;
    }

    // Method used to output representation of entity in bytes to output.
    public void writeTo(DataOutputStream output) throws IOException {
        output.writeShort(type.getValue());
        output.writeFloat(x);
        output.writeFloat(y);
        output.writeFloat(rotation);
    }

}
