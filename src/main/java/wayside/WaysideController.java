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

    static Decider decider;
    static TrackModelInterface tm = null;
    static WaysideUI gui = null;
    static WCStaticTrack st = null;

    /**
     * Initiallizes the WaysideController.
     * What is called for the application.
     * Creates a GUI, gets a handle to the real TrackModel,
     * and initializes my map to the Green Line.
     */
    public static void init() {
        gui = new WaysideUI();
        tm = TrackModel.getTrackModel();
        st = new WCStaticTrack(true);
        decider = new Decider(tm, st);
    }

    /** Displays the WC UI. */
    public static void openWindow() {
        gui.setVisible(true);
    }

    static void init(TrackModelInterface trackModel, WCStaticTrack staticTrack) {
        tm = trackModel;
        st = staticTrack;
        decider = new Decider(tm, st);
    }

    /** 
     * Transformation from what {@link CTCModel} gives to a linear list of authority.
     * @param suggestion list of suggestions, one per train.
     * @param numBlocks the number of blocks in this track.
     * @return linear representation of authority per block.
     * @throws UnsafeSuggestion in case I can determine the given suggestion is unsafe.
     */
    static boolean[] squash(Suggestion[] suggestion, int numBlocks) {
        boolean[] authority = new boolean[numBlocks];
        for (Suggestion s: suggestion) {
            if (s.authority == null) continue;
            for (int block: s.authority) {
                if (authority[block]) {
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
     * @param numBlocks the number of blocks on this track.
     * @return linear representation of speeds.
     */
    static int[] squashSpeed(Suggestion[] suggestion, int numBlocks) {
        int[] speed = new int[numBlocks];
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
        boolean[] authority = squash(suggestion, st.trackLen());
        int[] speed = squashSpeed(suggestion, st.trackLen());
        decider.suggest(authority, speed);

        for (int block = 0; block < st.trackLen(); block++) {
            tm.setAuthority(block, decider.getAuthority(block));
            tm.setSpeed(block, decider.getSpeed(block));
        }

        for (WCSwitch sw: st.getSwitches()) {
            tm.setSwitch(sw.root, decider.getSwitch(sw.root));
            tm.setSignal(sw.root, decider.getSignal(sw.root));
            tm.setSignal(sw.def, decider.getSignal(sw.def));
            tm.setSignal(sw.active, decider.getSignal(sw.active));
        }

        for (int cross: st.getCrossings()) {
            tm.setCrossingState(cross, decider.getCrossing(cross));
        }
    }

    /**
     * Checks the TrackModel to determine if a block is occupied.
     * @param blockId the line-specific number of the block in question.
     * @return the occupancy of the block, true if it is occupied, false otherwise.
     */
    public static boolean isOccupied(int blockId) {
        boolean o = tm.isOccupied(blockId);
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