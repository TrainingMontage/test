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
import wayside.WaysideController;

public class NoTrack {

    @Before
    public void init() {
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
    public void squshSpeed() {
        Suggestion[] s = new Suggestion[] {
            new Suggestion(2, 3, new int[] {2, 3})
        };
        int[] speed = WaysideController.squashSpeed(s);
        Assert.assertEquals(speed[1], 0);
        Assert.assertEquals(speed[2], 3);
        Assert.assertEquals(speed[3], 0);
    }
}