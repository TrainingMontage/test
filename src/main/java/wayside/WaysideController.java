package wayside;

import utils.BlockStatus;
import utils.Suggestion;
import wayside.TrackModel;
import wayside.WaysideControllerGUI;

import java.util.List;

/**
 * The public interface of all wayside controllers.
 * Since there are multiple WCs all around the track,
 * we represent each of them with an object, adressable by region.
 *
 * Key Use Case
 * ============
 * [Given that PLC has been uploaded]
 *  - Accept Suggested speed and authority per train.
 *  - Validate that this suggested input is safe (the primary job),
 *      pass it along to the TrackModel if it is.
 *      default to 0 otherwise.
 *  - Determine switches, signals, and crossing to excecute this plan.
 */
public class WaysideController {

    /** Stores the wayside controller objects, indexed by region number. */
    private static WC[] controllers;
    /** Maps the block ID to region number. */
    private static int[] regions;

    /**
     * Initiallizes the WaysideController.
     * For now, just working on one region.
     */
    public static void init() {
        controllers = new WC[1];
        controllers[0] = new WC();
        WaysideControllerGUI gui = new WaysideControllerGUI();
        TrackModel.init(gui);
        gui.setVisible(true);
        test_emptyTrackSafeSuggestion();
    }

    private static void test_emptyTrackSafeSuggestion() {
        int[] list = {0, 1, 2, 3, 4, 5};
        Suggestion s = new Suggestion(0, 10, list);
        Suggestion[] res = {s};
        WaysideController.suggest(res);
    }

    public static boolean isOccupied(int blockId) {
        return TrackModel.isOccupied(blockId);
    }

    public static boolean getSignal(int blockId) {
        return TrackModel.getSignal(blockId);
    }
    
    public static boolean getSwitch(int blockId) {
        return TrackModel.getSwitch(blockId);
    }
    
    public static boolean getCrossing(int blockId) {
        return TrackModel.getCrossing(blockId);
    }

    public static boolean getAuthority(int blockId) {
        return TrackModel.getTrainAuthority(blockId);
    }

    public static int getSpeed(int blockId) {
        return TrackModel.getTrainSpeed(blockId);
    }
    
    public static void suggest(Suggestion[] suggestion) {
        boolean needToMoveSwitch = false;
        
        // Validate that this is a safe suggestion.
        boolean safe = true;
        boolean[] safeAuthority = new boolean[TrackModel.TRACK_LEN];
        for (Suggestion s: suggestion) {
            for (int i = 0; i < s.authority.length; i++) {
                int block = s.authority[i];
                if (TrackModel.isOccupied(block) && block != s.blockId) {
                    safe = false; // Authority would run over some other train.
                    break;
                }
                if (safeAuthority[block]) {
                    safe = false; // overlapping authority
                    break;
                }
                if (block == TrackModel.ACTIVE_LEAF && i > 0 && s.authority[i-1] == TrackModel.SWITCH)
                    needToMoveSwitch = true;
                safeAuthority[block] = true;
            }
        }

        // Now act!
        if (safe) {
            for (int block = 0; block < TrackModel.TRACK_LEN; block++) {
                TrackModel.setAuthority(block, safeAuthority[block]);
                if (block == TrackModel.CROSSING && safeAuthority[block]) {
                    TrackModel.setCrossing(TrackModel.CROSSING, true);
                }
            }
            // The switch handling code needs to be paramaterized based on 
            // which direction the train needs to go.
            if (needToMoveSwitch) {
                TrackModel.setSwitch(TrackModel.SWITCH, true);
                TrackModel.setSignal(TrackModel.SWITCH, true);
                TrackModel.setSignal(TrackModel.ACTIVE_LEAF, true);
                TrackModel.setSignal(TrackModel.DEFAULT_LEAF, false);
            }
            for (Suggestion s: suggestion)
                TrackModel.setSpeed(s.blockId, s.speed);
        } else {
            for (int i = 0; i < TrackModel.TRACK_LEN; i++) {
                TrackModel.setAuthority(i, false);
                TrackModel.setSpeed(i, 0);
            }
        }
    }
    
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
 */
class WC {
    WC() {
        // TODO
    }
}