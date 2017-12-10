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

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import shared.Suggestion;
import trackmodel.TrackModel;
import wayside.WaysideController;

public class EmptyTrack {

    WCTrackModel tm;
    Decider decider;
    WCStaticTrack st;
    final int[] speed = {0,1,2,3,4,5,6,7,8};

    public EmptyTrack() {
        st = new WCStaticTrack(false);
        tm = new WCTrackModel(st.trackLen());
        tm.occupy(2, true);
        decider = new Decider(tm, st);
        WaysideController.init(tm, st);
    }

    @Test
    public void twoOccupied() {
        Assert.assertTrue(WaysideController.isOccupied(2));
    }

    @Test
    public void switchOff() {
        Assert.assertFalse(WaysideController.getSwitch(2));
    }

    @Test
    public void safeSwitchesDefault() {
        boolean[] authority = new boolean[] {
            false, true, true, true,
            false, false, false, false, false
        };
        assertTrue(decider.suggest(authority, speed));
        boolean[] expected = new boolean[st.trackLen()];
        for (int i = 0; i < st.trackLen(); i++) {
            Assert.assertEquals(expected[i], decider.getSwitch(i));
        }
    }

    @Test
    public void safeSwitchNoPathOver() {
        boolean[] authority = new boolean[] {
            false, false, false, false,
            true, true, true, false, false
        };
        boolean[] expected = new boolean[st.trackLen()];
        expected[2] = false;
        assertTrue(decider.suggest(authority, speed));
        for (int i = 0; i < st.trackLen(); i++) {
            Assert.assertEquals(expected[i], decider.getSwitch(i));
        }
        assertFalse(decider.getAuthority(3));
        assertTrue(decider.getAuthority(4));
        assertTrue(decider.getAuthority(6));
        assertFalse(decider.getAuthority(7));
    }

    @Test
    public void safeSwitchActive() {
        boolean[] authority = new boolean[] {
            false, true, true, false,
            false, false, false, false, true
        };
        boolean[] expected = new boolean[st.trackLen()];
        expected[2] = true;
        assertTrue(decider.suggest(authority, speed));
        for (int i = 0; i < st.trackLen(); i++) {
            Assert.assertEquals(expected[i], decider.getSwitch(i));
        }
    }

    @Test
    public void unsafeSwitches() {
        boolean[] authority = new boolean[] {
            false, false, false, true,
            false, false, false, false, true
        };
        Assert.assertFalse(decider.suggest(authority, speed));
    }

    @Test
    public void safeStrightLine() {
        boolean[] authority = new boolean[] {
            false, true, true, false,
            false, false, false, false, true
        };
        Assert.assertTrue(decider.suggest(authority, speed));
    }

    @Test
    public void safeBrokenLineToSelf() {
        boolean[] authority = new boolean[] {
            false, false, true, false,
            true, true, false, true, true
        };
        Assert.assertTrue(decider.suggest(authority, speed));
    }

    @Test
    public void unsafeUnbrokenLineToSelf() {
        // This should fail our switch rules anyway.
        boolean[] authority = new boolean[] {
            false, false, true, true,
            true, true, true, true, true
        };
        Assert.assertFalse(decider.suggest(authority, speed));
    }
}