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

import shared.Suggestion;
import trackmodel.TrackModel;
import wayside.trackmock.WCTrackModel;
import wayside.WaysideController;

import trackmodel.StaticTrack;

public class EmptyTrack {

    WCTrackModel tm;
    Decider decider;
    final int[] speed = {1,2,3,4,5,6,7,8};
    final boolean[] occupancy = {
        false, false, true, false, false, false, false, false, false
    };
    final int[][] PATHS = new int[][] {
        new int[] {1,2,3,4,5,6,7},
        new int[] {3,4,5,6,7,2,1}
    };

    public EmptyTrack() {
        tm = new WCTrackModel();
        tm.occupy(2, true);
        decider = new Decider(occupancy, PATHS);
        WaysideController.initTest(tm);
    }

    @Test
    public void trackModelSwitchPositions() {
        StaticTrack st = TrackModel.getTrackModel().getStaticTrack();
        Assert.assertEquals(2, st.getStaticSwitch(1).getRoot().getId());
    }

    @Test
    public void notOccupied() {
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
        decider.suggest(authority, speed);
        boolean[] expected = new boolean[WaysideController.TRACK_LEN];
        for (int i = 0; i < WaysideController.TRACK_LEN; i++) {
            Assert.assertEquals(expected[i], decider.getSwitch(i));
        }
    }

    @Test
    public void safeSwitchNoPathOver() {
        boolean[] authority = new boolean[] {
            false, false, false, false,
            true, true, true, false, false
        };
        boolean[] expected = new boolean[WaysideController.TRACK_LEN];
        decider.suggest(authority, speed);
        for (int i = 0; i < WaysideController.TRACK_LEN; i++) {
            Assert.assertEquals(expected[i], decider.getSwitch(i));
        }
    }

    @Test
    public void safeSwitchActive() {
        boolean[] authority = new boolean[] {
            false, true, true, false,
            false, false, false, false, true
        };
        boolean[] expected = new boolean[WaysideController.TRACK_LEN];
        expected[2] = true;
        decider.suggest(authority, speed);
        for (int i = 0; i < WaysideController.TRACK_LEN; i++) {
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