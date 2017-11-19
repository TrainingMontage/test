/*   ______                 _           _                 
 *  /_  __/ _____  ____ _  (_) ____    (_) ____    ____ _ 
 *   / /   / ___/ / __ `/ / / / __ \  / / / __ \  / __ `/ 
 *  / /   / /    / /_/ / / / / / / / / / / / / / / /_/ /  
 * /_/   /_/     \__,_/ /_/ /_/ /_/ /_/ /_/ /_/  \__, /   
 *                                              /____/    
 *     __  ___                 __                        
 *    /  |/  / ____    ____   / /_  ____ _  ____ _  ___ 
 *   / /|_/ / / __ \  / __ \ / __/ / __ `/ / __ `/ / _ \
 *  / /  / / / /_/ / / / / // /_  / /_/ / / /_/ / /  __/
 * /_/  /_/  \____/ /_/ /_/ \__/  \__,_/  \__, /  \___/ 
 *                                       /____/         
 *
 * @author Isaac Goss
 */

package wayside;

import shared.BlockStatus;
import shared.Suggestion;
import trackmodel.TrackModel;
import trackmodel.StaticBlock;
import trackmodel.StaticSwitch;
import wayside.UILayer;

import java.util.List;

/**
 * The public interface of all wayside controllers.
 * Since there are multiple WCs all around the track,
 * we represent each of them with an object, adressable by region.
 *
 * <h3> Key Use Case </h3>
 * 
 * <h4> [Given that PLC has been uploaded] </h4>
 * <ul>
 *  <li> Accept Suggested speed and authority per train.
 *  <li> Validate that this suggested input is safe (the primary job),
 *      pass it along to the TrackModel if it is.
 *      default to 0 otherwise.
 *  <li> Determine switches, signals, and crossing to excecute this plan.
 * </ul>
 */
public class WaysideController {

    /** Stores the wayside controller objects, indexed by region number. */
    private static WC[] controllers;
    /** Maps the block ID to region number. */
    private static int[] regions;

    /** 
     * A horrible hacky solution,
     * embedding information about the track in this module.
     * Also, this is only the Green line.
     */
    static int TRACK_LEN = 151;
    static int NUM_SWITCHES = 7;
    static int[] CROSSINGS = {19};
    
    static TrackModel tm = TrackModel.getTrackModel();

    /**
     * Initiallizes the WaysideController.
     * For now, just working on one region.
     */
    public static void init() {
        // TODO
    }

    /**
     * Initiallizes for JUnit testing, 
     * against "test_track.csv" instead of the real line.
     */
    public static void initTest() {
        TRACK_LEN = 9;
        NUM_SWITCHES = 2;
        CROSSINGS = null;
    }

    /**
     * Opens the WC UI.
     */
    public static void openWindow() {
        UILayer.init();
    }


    /**
     * Constructs and returns the Strings representing each WC by the ranges of blocks which they control.
     * Described in Issue #59.
     * @return the list of these strings.
     * @throws UnsupportedOperationException description
     */
    public static String[] getRanges() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * Checks the TrackModel to determine if a block is occupied.
     * @param blockId the line-specific number of the block in question.
     * @return the occupancy of the block, true if it is occupied, false otherwise.
     */
    public static boolean isOccupied(int blockId) {
        return tm.isOccupied(blockId);
    }

    /**
     * Checks the TrackModel to determine if a switch is active.
     * @param blockId the line-specific number of the block of the signal in question.
     * @return the state of the signal, true if it is active, false otherwise.
     */
    public static boolean getSignal(int blockId) {
        return tm.getSignal(blockId);
    }
    
    /**
     * Checks the TrackModel to determine if a switch is active.
     * @param blockId the line-specific number of the block of the switch in question.
     * @return the state of the swtich, true if it is active, false otherwise.
     */
    public static boolean getSwitch(int blockId) {
        return tm.getSwitch(blockId);
    }
    
    /**
     * Checks the TrackModel to determine if a block is occupied.
     * @param blockId the line-specific number of the block in question.
     * @return the occupancy of the block, true if it is occupied, false otherwise.
     */
    public static boolean getCrossing(int blockId) {
        return tm.getCrossingState(blockId);
    }

    /**
     * Checks the TrackModel to determine if a block has authority.
     * @param blockId the line-specific number of the block in question.
     * @return the authority of the block, true if it has authority, false otherwise.
     */
    public static boolean getAuthority(int blockId) {
        return tm.getTrainAuthority(blockId) != 0;
    }

    public static int getSpeed(int blockId) {
        return (int) tm.getTrainSpeed(blockId);
    }

    /** 
     * Transformation from what {@link CTCModel} gives to a linear list of authority.
     * @param suggestion list of suggestions, one per train.
     * @return linear representation of authority per block.
     * @throws RuntimeException in case I can determine the given suggestion is unsafe.
     */
    static boolean[] squash(Suggestion[] suggestion) {
        boolean[] authority = new boolean[TRACK_LEN];
        for (Suggestion s: suggestion) {
            for (int block: s.authority) {
                if (authority[block]) {
                    // TODO: make custom exception UnsafeSuggestion
                    throw new RuntimeException(String.format(
                        "Block %d was suggested twice", block
                    ));
                }
                authority[block] = true;
            }
        }
        return authority;
    }

    /**
     * Converts suggestion from {@link CTCModel} to linear layout of speed.
     * @param suggestion Suggestions per train.
     * @return linear representation of speeds.
     */
    static int[] squashSpeed(Suggestion[] suggestion) {
        int[] speed = new int[TRACK_LEN];
        for (Suggestion s: suggestion) {
            speed[s.blockId] = s.speed;
        }
        return speed;
    }

    /**
     * Given the linear authorty is safe around all switches.
     * @param authority the linear representation of authority.
     * @return the switch positions based on authority.
     * @throws RuntimeException if authority is found to be unsafe around switches.
     */
    static boolean[] checkAndSetSwitches(boolean[] authority) {
        boolean[] pos = new boolean[TRACK_LEN];
        for (int sw = 1; sw < NUM_SWITCHES; sw++) {
            StaticSwitch ss = tm.getStaticSwitch(sw);
            int root = ss.getRoot().getId();
            int def = ss.getDefaultLeaf().getId();
            int active = ss.getActiveLeaf().getId();
            if (authority[def] && authority[active]) {
                // both default and active branch cannot have authority
                throw new RuntimeException(String.format(
                    "Both leaves of a switch cannot be given authority.  " +
                    "Blocks %d and %d were given suggested authority", def, active
                ));
            }
            // which way should the switch go?
            if (authority[active]) {
                pos[root] = true;
            }
        }
        return pos;
    }

    // Another horrible hack just to get it "working"
    private static int[] getPath() {
        return new int[] {1,2,3,4,5,6,7,2,1};
    }

    /**
     * Implements the staight-line rules discussed before.
     * @param authority the linear representation of authority.
     * @return the crossing state, given that this authority is safe.
     * @throws RuntimeException if this suggestion is not found to be safe.
     */
    static boolean[] checkStraightLine(boolean[] authority) {
        // I need to see if I can find a path from one occupied block to another
        boolean unbrokenPath = false;
        int[] path = getPath();
        for (int block: path) {
            if (unbrokenPath) {
                if (wayside.TrackModel.isOccupied(block)) {
                    throw new RuntimeException(String.format(
                        "Found an unbroken path from some occupied block to %d", block
                    ));
                }
                // This doesn't follow the 2-block rule.
                unbrokenPath = authority[block];
            } else {
                if (wayside.TrackModel.isOccupied(block)) {
                    unbrokenPath = true;
                }
            }
        }
        return new boolean[TRACK_LEN];
    }
    
    /**
     * How CTC presents a suggestion of speed and authority for each train.
     * The form of this suggestion can be found in the {@link shared.Suggestion} class.
     * IMPLEMENTATION SUBJECT TO CHANGE.
     * TODO: throw custom Exception if the WC does not does not have PLC uploaded.
     * @param suggestion an array of Suggestion objects, one for each train.
     */
    public static void suggest(Suggestion[] suggestion) {
        boolean[] authority = squash(suggestion);
        int[] speed = squashSpeed(suggestion);
        boolean[] switchState = checkAndSetSwitches(authority);

        // check straight line
        // If valid, write suggested values.
        // Otherwise, write default values.
    }
    
    /**
     * Checks the TrackModel to determine the status of a block. NOT YET IMPLEMENTED!!
     * @param blockId the line-specific number of the block in question.
     * @return the status of the block, as represented by the BlockStatus enum defined elsewhere.
     */
    public static BlockStatus getStatus(int blockId) {
        // TODO
        return null;
    }

    private static int sum(int[] a) {
        int res = 0;
        for (int x : a) {
            res += x;
        }
        return res;
    }
}

/**
 * The objects which represent the actual WCs on the track.
 * This could be wrapped up in the WaysideController class,
 * but for simplicity, this class contains only object code,
 * and the WaysideController class holds only static code.
 *
 * NOT YET IMPLEMENTED!
 */
class WC {
    WC() {
        // TODO
    }
}