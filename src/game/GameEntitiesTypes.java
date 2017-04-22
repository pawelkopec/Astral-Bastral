package game;

/**
 * Created by micha on 22.04.2017.
 *
 * Enumeration for allowed entities types in the game.
 */
public enum GameEntitiesTypes {

    MAIN_SHIP(0),
    TURRET(1),
    FRIENDLY_MISSILE(2),
    ENEMY_MISSILE(3),
    ENEMY_SHIP(4),
    ASTEROID(5);


    private final int value;


    GameEntitiesTypes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
