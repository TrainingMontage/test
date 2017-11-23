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

public class WCStaticSwitch extends StaticSwitch {
    
    public WCStaticSwitch(int id) {
        super(id);
    }

    public void rootSetter(StaticBlock sb) {
        this.setRoot(sb);
    }

    public void activeLeafSetter(StaticBlock sb) {
        this.setActiveLeaf(sb);
    }

    public void defaultLeafSetter(StaticBlock sb) {
        this.setDefaultLeaf(sb);
    }
}