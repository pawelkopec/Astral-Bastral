package game;

/**
 * Created by micha on 22.04.2017.
 */
public enum MissilesTypes {

    EMPTY_MISSILE(-1);


    private final int value;


    MissilesTypes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
