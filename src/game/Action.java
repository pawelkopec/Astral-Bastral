package game;


import java.nio.ByteBuffer;

/**
 * Created by Paweł Kopeć on 21.03.17.
 *
 * Container class for player's action description.
 *
 * NOTES:
 * - Missile class may significantly change,
 * - There may be alternations to packet format such as
 *   additional parameters describing engine thurst added
 *   at the end of file.
 */
public class Action {

    // Client's packet describing message has prearranged format:
    //
    // [ (float) Rotation ] [ Array of sets of variables describing missiles ]
    // 0                    4                                                x
    //
    // where:
    // - x = MAX_MISSILES_PER_ACTION * BYTES_PER_MISSILE + 4,
    // - BYTES_PER_MISSILE = 2 (for short int type) +
    //                       4 (float rotation) +
    //                       8 (float x, y),
    // - and every missile in the array is described in the following manner:
    //
    // [ (short) type ] [ (float) rotation ] [ (float) x ] [ (float) y ]
    // 0                2                    6             10

    // Limits number of missile, which player can spawn in single action.
    // Might change later.
    private static final int MAX_MISSILES_PER_ACTION = 8;

    private float rotation;
    private Missile [] missilesToSpawn = new Missile[MAX_MISSILES_PER_ACTION];


    public Action(byte [] rawActionDescription) {

        // Create a ByteBuffer object from the provided byte array
        // to simplify the data extraction.
        ByteBuffer bufferedDescription = ByteBuffer.wrap(rawActionDescription);

        rotation = bufferedDescription.getFloat();

        // Loop, which extracts missiles data and fills missilesToSpawn array
        // with newly created Missile objects.
        short missileType;
        float missileX, missileY, missileRotation;
        for (int i = 0; i < MAX_MISSILES_PER_ACTION; i++) {

            // Extract data from buffer with respect
            // to previously given description.
            missileType = bufferedDescription.getShort();
            missileRotation = bufferedDescription.getFloat();
            missileX = bufferedDescription.getFloat();
            missileY = bufferedDescription.getFloat();

            // Player might have not spawned enough missiles in his action
            // to fill whole missilesToSpawnArray. In this case each missile
            // element in the array, that was not filled by player has its type
            // set to Missile.EMPTY_MISSILE_TYPE. If we encounter first such
            // missile in the byte set we can...
            if (missileType == Missile.EMPTY_MISSILE_TYPE) {

                // ... set all remaining missilesToSpawn array
                // elements to null and stop reading the buffer.
                for (int j = i; j < MAX_MISSILES_PER_ACTION; j++) {
                    missilesToSpawn[j] = null;
                }
                break;

            }
            // Else we simply set current missilesToSpawn element
            // to new Missile object reference.
            else {
                missilesToSpawn[i] = new Missile(
                    missileX,
                    missileY,
                    missileRotation
                );
            }

        }

    }


}
