package game;

/**
 * Created by micha on 22.04.2017.
 *
 * Enumeration for allowed entities types in the game.
 */
public enum GameEntitiesTypes {

    // Types of all entities used in the game.
    EMPTY_ENTITY((short) -1),
    MAIN_SHIP((short) 0),
    TURRET((short) 1),
    FRIENDLY_MISSILE((short) 2),
    ENEMY_MISSILE((short) 3),
    ENEMY_SHIP((short) 4),
    ASTEROID((short) 5);


    // Number representing type of entity.
    private final short value;


    GameEntitiesTypes(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

}
