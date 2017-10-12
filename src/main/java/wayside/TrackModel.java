package wayside;

import utils.BlockStatus;
import wayside.WaysideControllerGUI;

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
    private static WaysideControllerGUI gui;

    /** Needed to set up initial track state.
     * @param g The GUI, so that this layer can update the GUI whenever output changes.
     *      NOTE THAT THIS CAN BE NULL.
     */
    public static void init(WaysideControllerGUI g) {
        gui = g;
        gui.setOccupancy("occupancy");
        gui.setCrossing("crossing");
        gui.setSwitch("switch");
        gui.setSignal("signal");
        gui.setActualAuthority("actual authority");
        gui.setSuggestedAuthority("suggested authority");
        gui.setSuggestedSpeed("suggested speed");
        gui.setActualSpeed("actual speed");
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
     */
    public static boolean setOccupancy(int blockId, boolean occ) {
        occupancy[blockId] = occ;
        guiOccupancy();
        return occ;
    }

    private static void guiOccupancy() {
        if (gui == null) return; // Running headless.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TRACK_LEN; i++) {
            sb.append(i);
            sb.append(": ");
            sb.append(occupancy[i]);
            sb.append("\n");
        }
        gui.setOccupancy(sb.toString());
    }
    
    /** Cheating for this TrackModel; getting authority by block ID rather than Train ID. */
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
        guiAuthority();
        return auth;
    }

    private static void guiAuthority() {
        if (gui == null) return;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TRACK_LEN; i++) {
            if (authority[i]) {
                sb.append(i);
                sb.append(": ");
                sb.append(authority[i]);
                sb.append("\n");
            }
        }
        gui.setActualAuthority(sb.toString());
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