package wayside;

import java.util.List;

/**
 * The public interface of all wayside controllers.
 * Since there are multiple WCs all around the track,
 * we represent each of them with an object, adressable by region.
 */
public class WaysideController {

    /** Stores the wayside controller objects, indexed by region number. */
    private static WC[] controllers;
    /** Maps the block ID to region number. */
    private static int[] regions;

    public static void init() {
        // TODO
    }

    public static boolean isOccupied(int blockId) {
        // TODO
        return false;
    }

    public static boolean getSignal(int blockId) {
        // TODO
        return false;
    }
    
    public static boolean getSwitch(int blockId) {
        // TODO
        return false;
    }
    
    public static boolean getCrossing(int blockId) {
        // TODO
        return false;
    }
    
    public static void suggest(List<Suggestion> suggestion) {
        // TODO
    }
    
    public static BlockStatus getStatus(int blockId) {
        // TODO
        return null;
    }
}

class BlockStatus {}
class Suggestion {}

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