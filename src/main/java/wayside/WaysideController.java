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

import java.io.File;
import java.io.IOException;
import shared.BlockStatus;
import shared.Suggestion;
import trackmodel.TrackModel;
import trackmodel.TrackModelInterface;
import trackmodel.StaticBlock;
import trackmodel.StaticSwitch;
import trackmodel.StaticTrack;
import wayside.WaysideUI;

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
    public static int TRACK_LEN = 9;
    static int[] SWITCHES = new int[] {1};
    static int[] SWITCH_BLOCKS = new int[] {2};
    static int[] SWITCH_ACTIVE = new int[] {8};
    static int[] SWITCH_DEFAULT = new int[] {3};
    static int[] CROSSINGS = new int[] {};
    static int[][] PATHS = new int[][] {
        new int[] {1,2,3,4,5,6,7},
        new int[] {3,4,5,6,7,2,1}
    };

    static Decider decider;
    static TrackModelInterface tm = TrackModel.getTrackModel();
    static WaysideUI gui = null;
    static boolean[] occupancy = new boolean[TRACK_LEN];

    static void parseFile() {
        int INTO_YARD = 151;
        int FROM_YARD = 152;
        TRACK_LEN = 153;
        SWITCHES = new int[] {1, 2, 10, 11, 12, 13};
        SWITCH_BLOCKS  = new int[] {13,  28,  57,  63,  77,  85};
        SWITCH_ACTIVE  = new int[] { 1, 150, 151, 152, 101, 100};
        SWITCH_DEFAULT = new int[] {12,  29,  58,  62,  76,  86};
        CROSSINGS = new int[] {19};
        PATHS = new int[][] {
            // The long circuit around the entire track.
            new int[] {
                // high-C downto low-A,
                12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                // low-D upto high-Q,
                13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
                29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44,
                45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
                61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76,
                77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92,
                93, 94, 95, 96, 97, 98, 99, 100,
                // high-N downto low-N,
                85, 84, 83, 82, 81, 80, 79, 78, 77,
                // low-R upto high-Z
                101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113,
                114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126,
                127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139,
                140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150
            },
            // Leaving the yard, entering the track.
            new int[] {
                36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51,
                52, 53, 54, 55, 56, 57, INTO_YARD
            },
            // Entering the yard from the track.
            new int[] {
                FROM_YARD, 63, 64, 65, 66, 67, 68
            }
        };
        occupancy = new boolean[TRACK_LEN];
    }

    /**
     * Initiallizes the WaysideController.
     * For now, just working on the green line,
     * as though there's only one WC.
     */
    public static void init() {
        gui = new WaysideUI();
        parseFile();
    }

    /**
     * Initiallizes for JUnit testing, 
     * against "test_track.csv" instead of the real line.
     */
    public static void initTest() {
        // do nothing...
    }

    public static void initTest(TrackModelInterface t) {
        initTest();
        tm = t;
    }

    /**
     * Opens the WC UI.
     */
    public static void openWindow() {
        gui.setVisible(true);
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
        boolean o = occupancy[blockId];
        if (gui != null)
            gui.setOccupancy(blockId, o);
        return o;
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
        return tm.getTrainAuthority(blockId);
    }

    public static int getSpeed(int blockId) {
        return (int) tm.getTrainSpeed(blockId);
    }

    /** 
     * Transformation from what {@link CTCModel} gives to a linear list of authority.
     * @param suggestion list of suggestions, one per train.
     * @return linear representation of authority per block.
     * @throws UnsafeSuggestion in case I can determine the given suggestion is unsafe.
     */
    static boolean[] squash(Suggestion[] suggestion) {
        boolean[] authority = new boolean[TRACK_LEN];
        for (Suggestion s: suggestion) {
            if (s.authority == null) continue;
            for (int block: s.authority) {
                if (authority[block]) {
                    // TODO: make custom exception UnsafeSuggestion
                    throw new UnsafeSuggestion(String.format(
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
     * How CTC presents a suggestion of speed and authority for each train.
     * The form of this suggestion can be found in the {@link shared.Suggestion} class.
     * IMPLEMENTATION SUBJECT TO CHANGE.
     * TODO: throw custom Exception if the WC does not does not have PLC uploaded.
     * @param suggestion an array of Suggestion objects, one for each train.
     */
    public static void suggest(Suggestion[] suggestion) {
        boolean[] authority = squash(suggestion);
        int[] speed = squashSpeed(suggestion);
        decider.suggest(authority, speed);
        for (int block = 0; block < TRACK_LEN; block++) {
            tm.setAuthority(block, decider.getAuthority(block));
            tm.setSwitch(block, decider.getSwitch(block));
            tm.setSignal(block, decider.getSignal(block));
            tm.setSpeed(block, decider.getSpeed(block));
            tm.setCrossingState(block, decider.getCrossing(block));
        }
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

class UnsafeSuggestion extends RuntimeException {
    UnsafeSuggestion(String message) {
        super(message);
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