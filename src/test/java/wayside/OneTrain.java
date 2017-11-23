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

    final WCTrackModel tm;

    public OneTrain() {
        // trains on 1 and 5, I think
        tm = new WCTrackModel();
        tm.occupy(1, true);
        tm.occupy(5, true);
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
        try {
            WaysideController.checkStraightLine(authority);
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void unsafeStriaghtLine() {
        boolean[] authority = new boolean[] {
            false, true, true, true,
            true, true, false, false, false
        };
        try {
            WaysideController.checkStraightLine(authority);
            Assert.fail("Failed to find unsafe path between 2 occupied blocks.");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }
}