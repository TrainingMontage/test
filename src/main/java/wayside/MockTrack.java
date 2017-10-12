package wayside;

import utils.BlockStatus;

/**
 * A simple static track model aganist which to test WaysideController.
 *
 * We're looking at the following track:
 *  ========                switch          crossing
 *  | YARD |<---->  <---->  <---->  <----  <----
 *  --------                     \              \
 *                                \             /
 *                                  ---->  ---->
 *                                  ####o>
 * Where each dotted section is a block, as well as the pieces made of slashes.
 * You can see their directionality by the arrowheads; slash blocks can be inferred.
 * In a second, we'll be putting a train on block 4.
 */
public class MockTrack {

    public static final int TRACK_LEN = 9;
    private static boolean[] occupancy = new boolean[TRACK_LEN];
    private static boolean[] switches = new boolean[TRACK_LEN];
    private static boolean[] crossings = new boolean[TRACK_LEN];
    private static boolean[] signals = new boolean[TRACK_LEN];
    private static boolean[] authority = new boolean[TRACK_LEN];
    private static int[] speed = new int[TRACK_LEN];

    public static void init() {
        crossings = new boolean[TRACK_LEN];
        for (int i = 0; i < TRACK_LEN; i++) {
            occupancy[i] = false;
            setSwitch(i, false);
            setSignal(i, false);
            setCrossing(i, false);
            setAuthority(i, false);
            setSpeed(i, 0);
        }
    }
    
    /** Cheating for this MockTrack; getting authority by block ID rather than Train ID. */
    public static boolean getTrainAuthority(int trainId) {
        return authority[trainId];
    }

    public static int getTrainSpeed(int trainId) {
        return speed[trainId];
    }

    public static boolean setSignal(int blockId, boolean value) {
        signals[blockId] = value;
        return value;
    }

    public static boolean getSignal(int blockId) {
        return signals[blockId];
    }

    public static boolean setSwitch(int blockId, boolean value) {
        switches[blockId] = value;
        return value;
    }

    public static boolean getSwitch(int blockId) {
        return switches[blockId];
    }

    public static boolean setAuthority(int blockId, boolean auth) {
        authority[blockId] = auth;
        return auth;
    }

    public static int setSpeed(int blockId, int s) {
        speed[blockId] = s;
        return s;
    }

    public static boolean setCrossing(int blockId, boolean active) {
        crossings[blockId] = active;
        return active;
    }

    public static boolean getCrossing(int blockId) {
        return crossings[blockId];
    }

    public static byte[] getTrainBeacon(int trainId) {
        return null;
    }

    public static int getPassengers(int trainId) {
        return 0;
    }

    public static boolean isIcyTrack(int trainId) {
        return false;
    }
    
    public static StaticBlock getStaticBlock(int blockId) {
        return null;
    }

    public static double getGrade(int trainId) {
        return 0;
    }

    public static boolean isOccupied(int blockId) {
        return occupancy[blockId];
    }

    public static BlockStatus getStatus(int blockId) {
        return null;
    }
}

class StaticBlock {}