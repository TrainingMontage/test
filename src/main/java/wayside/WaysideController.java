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
import wayside.TrackModel;
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
     * Initiallizes the WaysideController.
     * For now, just working on one region.
     */
    public static void init() {
        // TODO
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
        return TrackModel.isOccupied(blockId);
    }

    /**
     * Checks the TrackModel to determine if a switch is active.
     * @param blockId the line-specific number of the block of the signal in question.
     * @return the state of the signal, true if it is active, false otherwise.
     */
    public static boolean getSignal(int blockId) {
        return TrackModel.getSignal(blockId);
    }
    
    /**
     * Checks the TrackModel to determine if a switch is active.
     * @param blockId the line-specific number of the block of the switch in question.
     * @return the state of the swtich, true if it is active, false otherwise.
     */
    public static boolean getSwitch(int blockId) {
        return TrackModel.getSwitch(blockId);
    }
    
    /**
     * Checks the TrackModel to determine if a block is occupied.
     * @param blockId the line-specific number of the block in question.
     * @return the occupancy of the block, true if it is occupied, false otherwise.
     */
    public static boolean getCrossing(int blockId) {
        return TrackModel.getCrossing(blockId);
    }

    /**
     * Checks the TrackModel to determine if a block has authority.
     * @param blockId the line-specific number of the block in question.
     * @return the authority of the block, true if it has authority, false otherwise.
     */
    public static boolean getAuthority(int blockId) {
        return TrackModel.getTrainAuthority(blockId);
    }

    public static int getSpeed(int blockId) {
        return TrackModel.getTrainSpeed(blockId);
    }
    
    /**
     * How CTC presents a suggestion of speed and authority for each train.
     * The form of this suggestion can be found in the {@link shared.Suggestion} class.
     * IMPLEMENTATION SUBJECT TO CHANGE.
     * TODO: throw custom Exception if the WC does not does not have PLC uploaded.
     * @param suggestion an array of Suggestion objects, one for each train.
     */
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