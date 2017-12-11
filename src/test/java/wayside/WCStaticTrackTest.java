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

import java.io.*;

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

    @Test
    public void greenLine() {
        st = new WCStaticTrack(true);

        assertEquals(153, st.trackLen());
        
        assertEquals(6, st.getSwitches().length);
        assertEquals(
            new WCSwitch( 1, 13, 12,   1),
            st.getSwitches()[0]
        );
        assertEquals(
            new WCSwitch( 2, 28, 29, 150),
            st.getSwitches()[1]
        );
        assertEquals(
            new WCSwitch(10, 57, 58, 151),
            st.getSwitches()[2]
        );
        assertEquals(
            new WCSwitch(11, 63, 62, 152),
            st.getSwitches()[3]
        );
        assertEquals(
            new WCSwitch(12, 77, 76, 101),
            st.getSwitches()[4]
        );
        assertEquals(
            new WCSwitch(13, 85, 86, 100),
            st.getSwitches()[5]
        );

        assertArrayEquals(
            new int[] {19},
            st.getCrossings()
        );

        assertArrayEquals(
            new int[] {
                36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,
                48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 151
            },
            st.getPaths()[1]
        );
        assertArrayEquals(
            new int[] {152, 63, 64, 65, 66, 67, 68},
            st.getPaths()[2]
        );
    }

    @Test
    public void uploadGreenLine() throws IOException, FailedToReadPlc {
        st = new WCStaticTrack(new File("src/main/resources/wayside/track.plc"));

        assertEquals(153, st.trackLen());
        
        assertEquals(6, st.getSwitches().length);
        assertEquals(
            new WCSwitch( 1, 13, 12,   1),
            st.getSwitches()[0]
        );
        assertEquals(
            new WCSwitch( 2, 28, 29, 150),
            st.getSwitches()[1]
        );
        assertEquals(
            new WCSwitch(10, 57, 58, 151),
            st.getSwitches()[2]
        );
        assertEquals(
            new WCSwitch(11, 63, 62, 152),
            st.getSwitches()[3]
        );
        assertEquals(
            new WCSwitch(12, 77, 76, 101),
            st.getSwitches()[4]
        );
        assertEquals(
            new WCSwitch(13, 85, 86, 100),
            st.getSwitches()[5]
        );

        assertArrayEquals(
            new int[] {19},
            st.getCrossings()
        );

        assertArrayEquals(
            new int[] {
                36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,
                48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 151
            },
            st.getPaths()[1]
        );
        assertArrayEquals(
            new int[] {152, 63, 64, 65, 66, 67, 68},
            st.getPaths()[2]
        );
    }
}