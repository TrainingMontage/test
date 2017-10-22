package wayside;

import utils.BlockStatus;

/**
 * A simple static track model aganist which to test WaysideController.
 */
public class TrackModel {

    public static final int TRACK_LEN = 9;
    public static final int SWITCH = 2;
    public static final int DEFAULT_LEAF = 8;
    public static final int ACTIVE_LEAF = 3;
    public static final int CROSSING = 7;

    private static boolean[] occupancy = new boolean[TRACK_LEN];
    private static boolean[] switches = new boolean[TRACK_LEN];
    private static boolean[] crossings = new boolean[TRACK_LEN];
    private static boolean[] signals = new boolean[TRACK_LEN];
    private static boolean[] authority = new boolean[TRACK_LEN];
    private static int[] speed = new int[TRACK_LEN];

    /** 
     * Needed to set up initial track state.
     */
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
        setSignal(SWITCH, true);
        setSignal(DEFAULT_LEAF, true);
    }

    /**
     * This exists only for this mock-up; not in the final code!!
     * @param blockId position of the train.
     * @param occ set the occupancy to either true or false.
     * @return the occupancy of that block after assignment.
     */
    public static boolean setOccupancy(int blockId, boolean occ) {
        occupancy[blockId] = occ;
        return occ;
    }
    
    /** 
     * Cheating for this TrackModel; getting authority by block ID rather than Train ID. 
     * @param trainId for now, the block on which this train resides.
     * @return the authority given to that block.
     */
    public static boolean getTrainAuthority(int trainId) {
        return authority[trainId];
    }

    /**
     * Gets train speed.
     * @param trainId for now, the block this train is on.
     * @return the signal value after assignment.
     */
    public static int getTrainSpeed(int trainId) {
        return speed[trainId];
    }

    /**
     * Sets the signal to specified value.
     * @param blockId the block to which this signal will be sent.
     * @param value the value to which to set the signal.
     * @return the signal value after assignment.
     */
    public static boolean setSignal(int blockId, boolean value) {
        if (!(blockId == SWITCH || blockId == DEFAULT_LEAF || blockId == ACTIVE_LEAF))
            return false;
        signals[blockId] = value;
        return value;
    }

    /**
     * Gets the signal at the specified block.
     * @param blockId position of the signal to be read.
     * @return the current value of the signal.
     */
    public static boolean getSignal(int blockId) {
        return signals[blockId];
    }

    /**
     * Sets the position of the specified switch.
     * @param blockId position of the switch.
     * @param value sets the switch to either the default (false) or active (true) position.
     * @return the switch value, after assignment.
     */
    public static boolean setSwitch(int blockId, boolean value) {
        if (blockId != SWITCH) return false;
        switches[blockId] = value;
        return value;
    }

    /**
     * Gets the position of the switch at given block.
     * @param blockId block of the switch.
     * @return the state of the switch.
     */
    public static boolean getSwitch(int blockId) {
        return switches[blockId];
    }

    /**
     * Sets the thing.
     * @param blockId position.
     * @param auth value.
     * @return value after set.
     */
    public static boolean setAuthority(int blockId, boolean auth) {
        authority[blockId] = auth;
        return auth;
    }

    /**
     * Sets the thing.
     * @param blockId position.
     * @param s value.
     * @return value after set.
     */
    public static int setSpeed(int blockId, int s) {
        speed[blockId] = s;
        return s;
    }

    /**
     * Sets the thing.
     * @param blockId position.
     * @param active value.
     * @return value after set.
     */
    public static boolean setCrossing(int blockId, boolean active) {
        if (blockId != CROSSING) return false;
        crossings[blockId] = active;
        return active;
    }

    /**
     * Gets the thing.
     * @param blockId postion.
     * @return the thing.
     */
    public static boolean getCrossing(int blockId) {
        return crossings[blockId];
    }

    /**
     * Gets the thing.
     * @param trainId postion.
     * @return the thing.
     */
    public static byte[] getTrainBeacon(int trainId) {
        return null;
    }

    /**
     * Gets the thing.
     * @param trainId postion.
     * @return the thing.
     */
    public static int getPassengers(int trainId) {
        return 0;
    }

    /**
     * Gets the thing.
     * @param trainId postion.
     * @return the thing.
     */
    public static boolean isIcyTrack(int trainId) {
        return false;
    }
    
    /**
     * Gets the thing.
     * @param blockId postion.
     * @return the thing.
     */
    public static StaticBlock getStaticBlock(int blockId) {
        return null;
    }

    /**
     * Gets the thing.
     * @param trainId postion.
     * @return the thing.
     */
    public static double getGrade(int trainId) {
        return 0;
    }

    /**
     * Gets the thing.
     * @param blockId postion.
     * @return the thing.
     */
    public static boolean isOccupied(int blockId) {
        return occupancy[blockId];
    }

    /**
     * Gets the thing.
     * @param blockId postion.
     * @return the thing.
     */
    public static BlockStatus getStatus(int blockId) {
        return null;
    }
}

class StaticBlock {}