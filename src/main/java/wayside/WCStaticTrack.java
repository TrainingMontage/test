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

public class WCStaticTrack {

    private int TRACK_LEN = 9;
    private WCSwitch[] SWITCHES = new WCSwitch[] {
        new WCSwitch(1, 2, 3, 8)
    };
    private int[] CROSSINGS = new int[] {};
    private int[][] PATHS = new int[][] {
        new int[] {1,2,3,4,5,6,7,8},
        new int[] {3,4,5,6,7,8,2,1}
    };

    public WCStaticTrack(boolean greenLine) {
        // nothing...
    }

    public int trackLen() {
        return TRACK_LEN;
    }

    public WCSwitch[] getSwitches() {
        return SWITCHES;
    }

    public int[] getCrossings() {
        return CROSSINGS;
    }

    public int[][] getPaths() {
        return PATHS;
    }

}