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

import trackmodel.TrackModel;

public class OneTrain {

    final boolean[] OCC = new boolean[] {
        false, true, false, false, false, true, false, false, false
    };

    @BeforeClass
    public static void init() {
        TrackModel.initWithTestData();
        WaysideController.initTest();
    }

    @Test
    public void safeStraightLine() {
        boolean[] authority = new boolean[] {
            false, true, true, true,
            false, true, false, false, false
        };
        try {
            WaysideController.checkStraightLine(authority, OCC);
            Assert.assertTrue(true);
        } catch (Exception e) {
            e.printStackTrace();
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
            WaysideController.checkStraightLine(authority, OCC);
            Assert.fail("Failed to find unsafe path between 2 occupied blocks.");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }
}