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
import wayside.WaysideController;
import wayside.UnsafeSuggestion;

public class NoTrack {

    WCStaticTrack st = new WCStaticTrack(false);

    @Test
    public void squashAuth() {
        Suggestion[] s = new Suggestion[] {
            new Suggestion(2, 3, new int[] {2, 3})
        };
        boolean[] authority = WaysideController.squash(s, st.trackLen());
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
            WaysideController.squash(s, st.trackLen());
            Assert.fail("Did not find unsafe suggestion unsafe");
        } catch (UnsafeSuggestion e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void goodMultiSquash() {
        Suggestion[] s = new Suggestion[] {
            new Suggestion(2, 3, new int[] {2,3}),
            new Suggestion(5, 3, new int[] {5,6})
        };
        boolean[] auth = WaysideController.squash(s, st.trackLen());
        boolean[] expected = new boolean[] {
            false, false, true, true,
            false, true, true, false, false
        };
        Assert.assertArrayEquals(expected, auth);
    }

    @Test
    public void squshSpeed() {
        Suggestion[] s = new Suggestion[] {
            new Suggestion(2, 3, new int[] {2, 3})
        };
        int[] speed = WaysideController.squashSpeed(s, st.trackLen());
        Assert.assertEquals(0, speed[1]);
        Assert.assertEquals(3, speed[2]);
        Assert.assertEquals(0, speed[3]);
    }

    @Test
    public void squashNullAuthority() {
        Suggestion[] s = new Suggestion[] {
            new Suggestion(2, 3, null)
        };
        boolean[] authority = WaysideController.squash(s, st.trackLen());
        Assert.assertArrayEquals(new boolean[st.trackLen()], authority);
    }
}