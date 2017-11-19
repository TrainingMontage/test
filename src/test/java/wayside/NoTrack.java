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
import org.junit.Before;
import org.junit.Test;

import shared.Suggestion;
import trackmodel.TrackModel;
import wayside.WaysideController;

public class NoTrack {

    @Before
    public void init() {
        TrackModel.initWithTestData();
        WaysideController.initTest();
    }

    @Test
    public void squashAuth() {
        Suggestion[] s = new Suggestion[] {
            new Suggestion(2, 3, new int[] {2, 3})
        };
        boolean[] authority = WaysideController.squash(s);
        Assert.assertFalse(authority[1]);
        Assert.assertTrue(authority[2]);
        Assert.assertTrue(authority[3]);
        Assert.assertFalse(authority[4]);
    }

    @Test
    public void badSquash() {
        Suggestion[] s = new Suggestion[] {
            new Suggestion(2, 3, new int[] {2, 3, 4}),
            new Suggestion(3, 4, new int[] {3, 4, 5})
        };
        try {
            WaysideController.squash(s);
            Assert.fail("Did not find unsafe suggestion unsafe");
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void squshSpeed() {
        Suggestion[] s = new Suggestion[] {
            new Suggestion(2, 3, new int[] {2, 3})
        };
        int[] speed = WaysideController.squashSpeed(s);
        Assert.assertEquals(0, speed[1]);
        Assert.assertEquals(3, speed[2]);
        Assert.assertEquals(0, speed[3]);
    }
}