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
    
    WCTrackModel tm = new WCTrackModel(WaysideController.TRACK_LEN);
    
    @BeforeClass
    public static void setup() {
        WaysideController.greenLine();
    }

    public GreenLine() {
        WaysideController.initTest(tm);
    }

    @Test
    public void simpleOneTrainSafe() {
        tm.occupy(64, true);
        Suggestion[] s = new Suggestion[] {
            new Suggestion(64, 10, new int[] {64, 65, 66})
        };

        boolean[] auth = WaysideController.squash(s);
        boolean[] expected_auth = new boolean[WaysideController.TRACK_LEN];
        expected_auth[64] = true;
        expected_auth[65] = true;
        expected_auth[66] = true;
        assertArrayEquals(expected_auth, auth);

        int[] speed = WaysideController.squashSpeed(s);
        int[] expected_speed = new int[WaysideController.TRACK_LEN];
        expected_speed[64] = 10;
        assertArrayEquals(expected_speed, speed);

        boolean[] occ = new boolean[WaysideController.TRACK_LEN];
        occ[64] = true;

        assertEquals(3, WaysideController.PATHS.length);
        assertEquals(6, WaysideController.SWITCHES.length);

        Decider d = new Decider(occ, WaysideController.PATHS, WaysideController.SWITCHES);
        assertTrue(d.suggest(auth, speed));

        for (WCSwitch sw: WaysideController.SWITCHES) {
            assertFalse(d.getSwitch(sw.root));
        }

        assertTrue(d.getAuthority(64));
        assertTrue(d.getAuthority(65));
        assertTrue(d.getAuthority(66));
    }

}