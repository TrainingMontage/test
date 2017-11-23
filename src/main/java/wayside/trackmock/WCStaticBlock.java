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

package wayside.trackmock;

import trackmodel.StaticBlock;
import trackmodel.StaticSwitch;

/**
 * Used for testing and individual module.
 */
public class WCStaticBlock extends StaticBlock {

    /**
     * Constructor.
     * @param id the ID of this block.
     */
    public WCStaticBlock(int id) {
        this.setId(id);
    }

    /**
     * Sets the thing.
     * @param sw If this block is the root, or either leaf of a switch,
     *      call with said switch.
     */
    public void switchSetter(StaticSwitch sw) {
        setStaticSwitch(sw);
    }
}