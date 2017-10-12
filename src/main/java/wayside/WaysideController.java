package wayside;

import utils.BlockStatus;
import utils.Suggestion;
import wayside.TrackModel;

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
        int sw = 2;
        Suggestion s = suggestion[0]; // There should only be 1 for the 1 train so far.
        for (int block: s.authority) {
            TrackModel.setAuthority(block, true);
        }
        TrackModel.setSwitch(sw, true);
        TrackModel.setSpeed(0, s.speed);
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