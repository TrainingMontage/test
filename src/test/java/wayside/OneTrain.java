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

import java.util.Arrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.mockito.Mockito;

import trackmodel.TrackModel;
import trackmodel.TrackModelInterface;
import wayside.trackmock.WCTrackModel;

public class OneTrain {

    WCTrackModel tm;
    Decider decider;
    final int[] speed = {1,2,3,4,5,6,7,8};
    final boolean[] occupancy = {
        false, false, true, false, false, true, false, false, false
    };
    final int[][] PATHS = new int[][] {
        new int[] {1,2,3,4,5,6,7},
        new int[] {3,4,5,6,7,2,1}
    };

    public OneTrain() {
        // trains on 1 and 5, I think
        tm = new WCTrackModel();
        tm.occupy(1, true);
        tm.occupy(5, true);
        decider = new Decider(occupancy, PATHS);
        WaysideController.initTest(tm);
    }

    @Before
    public void clear() {
        tm.clear();
    }

    @Test
    public void safeStraightLine() {
        boolean[] authority = new boolean[] {
            false, true, true, true,
            false, true, false, false, false
        };
        Assert.assertTrue(decider.suggest(authority, speed));
    }

    @Test
    public void unsafeStriaghtLine() {
        boolean[] authority = new boolean[] {
            false, true, true, true,
            true, true, true, false, false
        };
        Assert.assertFalse(decider.suggest(authority, speed));
    }
}