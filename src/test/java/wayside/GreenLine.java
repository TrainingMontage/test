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

import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import shared.Suggestion;

public class GreenLine {
    
    WCStaticTrack st = new WCStaticTrack(true);
    WCTrackModel tm = new WCTrackModel(st.trackLen());
    Decider decider = new Decider(tm, st);

    @Test
    public void simpleOneTrainSafe() {
        tm.occupy(64, true);
        Suggestion[] s = new Suggestion[] {
            new Suggestion(64, 10, new int[] {64, 65, 66})
        };

        boolean[] auth = WaysideController.squash(s, st.trackLen());
        int[] speed = WaysideController.squashSpeed(s, st.trackLen());

        assertTrue(decider.suggest(auth, speed));

        for (WCSwitch sw: st.getSwitches()) {
            assertFalse(decider.getSwitch(sw.root));
        }

        for (int i = 1; i < st.trackLen(); i++) {
            assertEquals(auth[i], decider.getAuthority(i));
        }
    }

    @Test
    public void safeTwoTrainsAroundASwitch() {
        tm.occupy(152, true);
        tm.occupy(66, true);
        Suggestion[] s = new Suggestion[] {
            new Suggestion(152, 10, new int[] {152, 63}),
            new Suggestion(66, 10, new int[] {66, 67, 68})
        };

        boolean[] auth = WaysideController.squash(s, st.trackLen());
        int[] speed = WaysideController.squashSpeed(s, st.trackLen());

        assertTrue(decider.suggest(auth, speed));
        assertTrue(decider.getSwitch(63));
        for (int i = 1; i < st.trackLen(); i++) {
            assertEquals(auth[i], decider.getAuthority(i));
        }
    }

}