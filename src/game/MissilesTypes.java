package game;

/**
 * Created by micha on 22.04.2017.
 *
 * Enum which contains available missiles types.
 */
public enum MissilesTypes {

    // Available missiles types instances.
    EMPTY_MISSILE((short) -1),
    BASIC_MISSILE((short) 0);


    // Variable which contains particular missile type numeric representation.
    private final short value;


    MissilesTypes(short value) {
        this.value = value;
    }

    public short getValue() {
        return value;
    }

}
