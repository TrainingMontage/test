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

import trackmodel.TrackModelInterface;

public class OneTrain {

    WCStaticTrack st = new WCStaticTrack(false);
    WCTrackModel tm = new WCTrackModel(st.trackLen());
    Decider decider = new Decider(tm, st);
    final int[] speed = {0,1,2,3,4,5,6,7,8};

    public OneTrain() {
        tm.occupy(1, true);
        tm.occupy(5, true);
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