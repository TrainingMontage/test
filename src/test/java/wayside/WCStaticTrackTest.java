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
import static org.junit.Assert.*;

import shared.Suggestion;
import wayside.WaysideController;

public class WCStaticTrackTest {

    WCStaticTrack st;

    @Test
    public void sampleTrack() {
        st = new WCStaticTrack(false);
        assertEquals(9, st.trackLen());

        assertEquals(1, st.getSwitches().length);
        assertEquals(
            new WCSwitch(1, 2, 3, 8),
            st.getSwitches()[0]
        );

        assertEquals(0, st.getCrossings().length);

        assertArrayEquals(
            new int[] {1,2,3,4,5,6,7,8}, 
            st.getPaths()[0]
        );
        assertArrayEquals(
            new int[] {3,4,5,6,7,8,2,1},
            st.getPaths()[1]
        );
    }
}