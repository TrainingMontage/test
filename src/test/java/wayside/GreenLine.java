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
    boolean[] auth;
    int[] speed;

    private void squash(Suggestion[] s) {
        auth = WaysideController.squash(s, st.trackLen());
        speed = WaysideController.squashSpeed(s, st.trackLen());
    }

    @Test
    public void simpleOneTrainSafe() {
        tm.occupy(64, true);
        squash(new Suggestion[] {
            new Suggestion(64, 10, new int[] {64, 65, 66})
        });

        assertTrue(decider.suggest(auth, speed));
        for (int i = 1; i < st.trackLen(); i++) {
            assertEquals(auth[i], decider.getAuthority(i));
            assertEquals(speed[i], decider.getSpeed(i));
        }
    }

    @Test
    public void safeTwoTrainsAroundASwitch() {
        tm.occupy(152, true);
        tm.occupy(66, true);
        squash(new Suggestion[] {
            new Suggestion(152, 10, new int[] {152, 63}),
            new Suggestion(66, 10, new int[] {66, 67, 68})
        });

        assertTrue(decider.suggest(auth, speed));
        assertTrue(decider.getSwitch(63));
        for (int i = 1; i < st.trackLen(); i++) {
            assertEquals(auth[i], decider.getAuthority(i));
            assertEquals(speed[i], decider.getSpeed(i));
        }
    }

    @Test
    public void unsafeTwoTrainsAroundASwitch() {
        tm.occupy(152, true);
        tm.occupy(65, true);
        squash(new Suggestion[] {
            new Suggestion(152, 10, new int[] {152, 63, 64}),
            new Suggestion(65, 10, new int[] {65, 66, 67})
        });

        assertFalse(decider.suggest(auth, speed));
        for (int i = 1; i < st.trackLen(); i++) {
            assertFalse(decider.getAuthority(i));
            assertEquals(0, decider.getSpeed(i));
        }
    }

    @Test
    public void unsafeBothLeavesOfSwitch() {
        tm.occupy(152, true);
        tm.occupy(61, true);
        squash(new Suggestion[] {
            new Suggestion(152, 10, new int[] {152}),
            new Suggestion(61, 10, new int[] {61, 62})
        });

        assertFalse(decider.suggest(auth, speed));
    }

    @Test
    public void safeTrainOnTwoBlocks() {
        tm.occupy(70, true);
        tm.occupy(71, true);
        squash(new Suggestion[] {
            new Suggestion(71, 10, new int[] {71, 72, 73})
        });

        assertTrue(decider.suggest(auth, speed));
    }

    @Test
    public void twoSuggActiveThenDefaultSwitch() {
        tm.occupy(152, true);
        squash(new Suggestion[] {
            new Suggestion(152, 10, new int[] {152, 63, 64})
        });
        assertTrue(decider.suggest(auth, speed));
        assertTrue(decider.getSwitch(63));
        tm.occupy(152, false);
        tm.occupy(62, true);
        squash(new Suggestion[] {
            new Suggestion(62, 10, new int[] {62, 63, 64})
        });
        assertTrue(decider.suggest(auth, speed));
        assertFalse(decider.getSwitch(63));
    }

}