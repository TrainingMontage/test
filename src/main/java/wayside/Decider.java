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

import java.util.Arrays;

/** Also known as a virtual machine. */
public class Decider {

    /**
     * Wraps all the values I want to assign to a block of track.
     */
    private class TrackState {
        private boolean switchState;
        private boolean authority;
        private boolean crossing;
        private boolean signal;
        private int speed;
    }

    private boolean[] occupied;
    private int[] suggestedSpeed;
    private boolean[] suggestedAuthority;
    private TrackState[] track;
    private int[][] paths;

    /**
     * Construct a machine which does the work of determining a safe suggestion.
     * @param occupancy the current occupancy of the blocks under the control of this WC.
     *      These will be numbered consecutively from 0;
     *      {@link WaysideContoller} will be reponsible for resolving that numbering with blockIds.
     * @param paths the paths through this WC's purview.
     *      This will be a list of <b>local</b> block numbers.
     *      The order of the numbers in each subarray will represent a valid path for a train.
     */
    public Decider(boolean[] occupancy, int[][] paths) {
        occupied = Arrays.copy(occupancy);
        this.paths = paths; // could copy this.
        track = new TrackState[occupancy.length];
        for (int i = 0; i < occupancy.length; i++) {
            track = new TrackState();
        }
    }

    /**
     * Decides if the suggestion is safe,
     * and also calulates the necessary state.
     * @param authority the linear representation of suggested authority.
     * @param speed linear repr of suggested speed.
     * @return is this suggestion safe?
     */
    public boolean suggest(boolean[] authority, boolean[] speed) {
        suggestedAuthority = authority;
        suggestedSpeed = speed;
        if (checkSwitches() && checkStraightLine())
            return true; // All good.
        // assign the defaults.
        for (int i = 0; i < track.length; i++) {
            track[i] = new TrackState();
        }
        return false;
    }

    /**
     * Implements the staight-line rules discussed before.
     *      TODOs: 2-block rule. Calculate crossings.
     * @return whether this authority is safe.
     */
    boolean checkStraightLine() {
        // I need to see if I can find a path from one occupied block to another
        boolean unbrokenPath = false;

        for (int[] path: paths) {
            for (int block: path) {
                if (unbrokenPath) {
                    if (occupied[block]) {
                        return false;
                    }
                    // This doesn't follow the 2-block rule.
                    unbrokenPath = authority[block];
                } else {
                    if (occupied[block]) {
                        unbrokenPath = true;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the linear authorty is safe around all switches.
     * @return Wether the suggested authority is safe for switches.
     */
    boolean checkSwitches() {
        // for all switches...
        int root = 2;
        int def = 3;
        int active = 8;
        if (authority[def] && authority[active]) {
            // both default and active branch cannot have authority
            return false;
        }
        // which way should the switch go?
        if (authority[active]) {
            track[root].switchState = true;
        }
        return true;
    }
}
