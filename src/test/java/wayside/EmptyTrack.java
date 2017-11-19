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
import wayside.WaysideController;
import trackmodel.TrackModel;

public class EmptyTrack {
    
    @BeforeClass
    public static void init() {
        TrackModel.initWithTestData();
        wayside.TrackModel.setOccupancy(2, true);
        WaysideController.initTest();
    }

    @Test
    public void notOccupied() {
        Assert.assertFalse(WaysideController.isOccupied(2));
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
        boolean[] switchPos = WaysideController.checkAndSetSwitches(authority);
        boolean[] expected = new boolean[WaysideController.TRACK_LEN];
        Assert.assertArrayEquals(expected, switchPos);
    }

    @Test
    public void safeSwitchNoPathOver() {
        boolean[] authority = new boolean[] {
            false, false, false, false,
            true, true, true, false, false
        };
        boolean[] switchPos = WaysideController.checkAndSetSwitches(authority);
        boolean[] expected = new boolean[WaysideController.TRACK_LEN];
        Assert.assertArrayEquals(expected, switchPos);
    }

    @Test
    public void safeSwitchActive() {
        boolean[] authority = new boolean[] {
            false, true, true, false,
            false, false, false, false, true
        };
        boolean[] expected = new boolean[WaysideController.TRACK_LEN];
        expected[2] = true;
        boolean[] switchPos = WaysideController.checkAndSetSwitches(authority);
        Assert.assertArrayEquals(expected, switchPos);
    }

    @Test
    public void unsafeSwitches() {
        boolean[] authority = new boolean[] {
            false, false, false, true,
            false, false, false, false, true
        };
        try {
            WaysideController.checkAndSetSwitches(authority);
            Assert.fail("Did not find unsafe authority unsafe around switch 1");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void safeStrightLine() {
        boolean[] authority = new boolean[] {
            false, true, true, false,
            false, false, false, false, true
        };
        WaysideController.checkStraightLine(authority);
        Assert.assertTrue(true);
    }

    @Test
    public void safeBrokenLineToSelf() {
        boolean[] authority = new boolean[] {
            false, false, true, false,
            true, true, false, true, true
        };
        try {
            WaysideController.checkStraightLine(authority);
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void unsafeUnbrokenLineToSelf() {
        // This should fail our switch rules anyway.
        boolean[] authority = new boolean[] {
            false, false, true, true,
            true, true, true, true, true
        };
        try {
            WaysideController.checkStraightLine(authority);
            Assert.fail("Failed to find unsafe self loop unsafe.");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }
}