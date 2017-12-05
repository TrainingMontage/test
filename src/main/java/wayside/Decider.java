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
    private WCSwitch[] switches;

    /**
     * Construct a machine which does the work of determining a safe suggestion.
     * @param occupancy the current occupancy of the blocks under the control of this WC.
     *      These will be numbered consecutively from 0;
     *      {@link WaysideContoller} will be reponsible for resolving that numbering with blockIds.
     * @param paths the paths through this WC's purview.
     *      This will be a list of <b>local</b> block numbers.
     *      The order of the numbers in each subarray will represent a valid path for a train.
     * @param switches a listing of all switches on this track.
     */
    public Decider(boolean[] occupancy, int[][] paths, WCSwitch[] switches) {
        occupied = occupancy;
        this.paths = paths; // could copy this.
        track = new TrackState[occupancy.length];
        for (int i = 0; i < occupancy.length; i++) {
            track[i] = new TrackState();
        }
        this.switches = switches;
    }

    /**
     * A different constructor for testing.
     * @param occupancy same as before.
     * @param paths same as before.
     */
    public Decider(boolean[] occupancy, int[][] paths) {
        this(occupancy, paths, new WCSwitch[] {
            new WCSwitch(1, 2, 3, 8)
        });
    }

    /**
     * Decides if the suggestion is safe,
     * and also calulates the necessary state.
     * @param authority the linear representation of suggested authority.
     * @param speed linear repr of suggested speed.
     * @return is this suggestion safe?
     */
    public boolean suggest(boolean[] authority, int[] speed) {
        assert (authority.length == speed.length);

        suggestedAuthority = authority;
        suggestedSpeed = speed;
        if (checkSwitches() && checkStraightLine()) {
            for (int i = 0; i < track.length; i++) {
                track[i].authority = suggestedAuthority[i];
                track[i].speed = suggestedSpeed[i];
            }
            return true; // All good.
        }
        // assign the defaults.
        for (int i = 0; i < track.length; i++) {
            track[i] = new TrackState();
            // crossings if train is already over them?
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
                    unbrokenPath = suggestedAuthority[block];
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
        for (WCSwitch sw: switches) {
            int root = sw.root;
            int def = sw.def;
            int active = sw.active;
            if (suggestedAuthority[def] && suggestedAuthority[active]) {
                // both default and active branch cannot have authority
                return false;
            }
            // which way should the switch go?
            if (suggestedAuthority[active]) {
                track[root].switchState = true;
            }
        }
        return true;
    }

    boolean isOccupied(int blockId) {
        return occupied[blockId];
    }

    boolean getCrossing(int blockId) {
        return track[blockId].crossing;
    }

    boolean getSwitch(int blockId) {
        return track[blockId].switchState;
    }

    boolean getSignal(int blockId) {
        return track[blockId].signal;
    }

    boolean getAuthority(int blockId) {
        return track[blockId].authority;
    }

    int getSpeed(int blockId) {
        return track[blockId].speed;
    }
}
