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

import trackmodel.TrackModelInterface;

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

    private int[] suggestedSpeed;
    private boolean[] suggestedAuthority;
    private TrackState[] track;
    private TrackModelInterface tm;
    private WCStaticTrack st;

    /**
     * Construct a machine which does the work of determining a safe suggestion.
     * @param trackModel used for dynamic information about the state of the track.
     *      Used exclusively for <code>isOccupied()</code>.
     * @param staticTrack used for static information about the track, such as 
     *      number of blocks, paths through the track, switch information.
     */
    public Decider(TrackModelInterface trackModel, WCStaticTrack staticTrack) {
        assert trackModel != null;
        assert staticTrack != null;

        tm = trackModel;
        st = staticTrack;
        track = new TrackState[st.trackLen()];
        for (int i = 0; i < st.trackLen(); i++) {
            track[i] = new TrackState();
        }
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

        for (int[] path: st.getPaths()) {
            unbrokenPath = false;
            for (int i = 0; i < path.length-1; i++) {
                int block = path[i];
                if (unbrokenPath) {
                    if (tm.isOccupied(block)) {
                        return false;
                    }
                    // This doesn't follow the 2-block rule.
                    unbrokenPath = suggestedAuthority[block];
                } else {
                    if (tm.isOccupied(block) && !tm.isOccupied(path[i+1])) {
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
        for (WCSwitch sw: st.getSwitches()) {
            if (suggestedAuthority[sw.def] && suggestedAuthority[sw.active]) {
                // both default and active branch cannot have authority
                return false;
            }
            // which way should the switch go?
            if (suggestedAuthority[sw.active]) {
                track[sw.root].switchState = true;
            } else if (suggestedAuthority[sw.root] || suggestedAuthority[sw.def]) {
                track[sw.root].switchState = false;
            }
        }
        return true;
    }

    boolean isOccupied(int blockId) {
        return tm.isOccupied(blockId);
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
