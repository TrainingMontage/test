/*   ______                 _           _
 *  /_  __/ _____  ____ _  (_) ____    (_) ____    ____ _
 *   / /   / ___/ / __ `/ / / / __ \  / / / __ \  / __ `/
 *  / /   / /    / /_/ / / / / / / / / / / / / / / /_/ /
 * /_/   /_/     \__,_/ /_/ /_/ /_/ /_/ /_/ /_/  \__, /
 *     __  ___                 __               /____/
 *    /  |/  / ____    ____   / /_  ____ _  ____ _  ___
 *   / /|_/ / / __ \  / __ \ / __/ / __ `/ / __ `/ / _ \
 *  / /  / / / /_/ / / / / // /_  / /_/ / / /_/ / /  __/
 * /_/  /_/  \____/ /_/ /_/ \__/  \__,_/  \__, /  \___/
 *                                       /____/
 *
 * @author Alec Rosenbaum
 */

package trackmodel;

import java.util.HashMap;

/**
 * Class for static track.
 */
public class StaticTrack {
    private int id;
    private String region;
    private double grade;
    private double elevation;
    private double length;
    private String station;
    private HashMap<Integer, StaticSwitch> staticSwitches;
    private HashMap<Integer, StaticBlock> staticBlocks;

    /**
     * Constructs the object.
     */
    protected StaticTrack() {
        staticSwitches = new HashMap<Integer, StaticSwitch>();
        staticBlocks = new HashMap<Integer, StaticBlock>();
    }

    /**
     * Puts a static switch.
     *
     * @param      staticSwitch  The static switch
     */
    protected void putStaticSwitch(StaticSwitch staticSwitch) {
        staticSwitches.put(staticSwitch.getId(), staticSwitch);
    }

    /**
     * Puts a static block.
     *
     * @param      staticBlock  The static block
     */
    protected void putStaticBlock(StaticBlock staticBlock) {
        staticBlocks.put(staticBlock.getId(), staticBlock);
    }

    /**
     * Gets the static switch with a specified id.
     *
     * @param      id    The identifier
     *
     * @return     The static switch.
     */
    public StaticSwitch getStaticSwitch(int id) {
        return staticSwitches.get(id);
    }

    /**
     * Gets the static block with a specified id.
     *
     * @param      id    The identifier
     *
     * @return     The static block.
     */
    public StaticBlock getStaticBlock(int id) {
        return staticBlocks.get(id);
    }
}