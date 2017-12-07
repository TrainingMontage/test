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

public class WCSwitch {
    public final int id;
    public final int root;
    public final int def;
    public final int active;

    public WCSwitch(int id, int root, int def, int active) {
        this.id = id;
        this.root = root;
        this.def = def;
        this.active = active;
    }

    public boolean equals(Object other) {
        if (other instanceof WCSwitch) {
            WCSwitch that = (WCSwitch) other;
            return that.id == this.id &&
                that.root == this.root &&
                that.def == this.def &&
                that.active == this.active;
        }
        return false;
    }
}