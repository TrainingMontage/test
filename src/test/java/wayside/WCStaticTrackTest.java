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
    public void uploadWholeTrack() throws IOException, FailedToReadPlc {
        st = new WCStaticTrack(new File("src/main/resources/wayside/track.plc"));

        assertEquals(230, st.trackLen());
        
        assertEquals(13, st.getSwitches().length);
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
        assertEquals(
            new WCSwitch( 8, 169, 168, 154 ),
            st.getSwitches()[6]
        );
        assertEquals(
            new WCSwitch( 9, 162, 163, 153 ),
            st.getSwitches()[7]
        );
        assertEquals(
            new WCSwitch( 3, 180, 181, 229 ),
            st.getSwitches()[8]
        );
        assertEquals(
            new WCSwitch( 4, 186, 185, 225 ),
            st.getSwitches()[9]
        );
        assertEquals(
            new WCSwitch( 5, 190, 191, 224 ),
            st.getSwitches()[10]
        );
        assertEquals(
            new WCSwitch( 6, 197, 196, 220 ),
            st.getSwitches()[11]
        );
        assertEquals(
            new WCSwitch( 7, 205, 206, 219 ),
            st.getSwitches()[12]
        );


        assertArrayEquals(
            new int[] {19, 200},
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
        assertArrayEquals(
            new int[] {
                176, 175, 174, 173, 172, 171, 170, 169, 168, 167, 166,
                165, 164, 163, 162, 161, 160, 159, 158, 157, 156, 155, 154
            },
            st.getPaths()[3]
        );
        assertArrayEquals(
            new int[] {
                153, 162, 161, 160, 159, 158, 157, 156, 155, 154, 185,
                184, 183, 182, 181, 180, 179, 178, 177, 176, 175, 174
            },
            st.getPaths()[4]
        );
        assertArrayEquals(
            new int[] {
                201, 200, 199, 198, 197, 196, 195, 194, 193, 192, 191,
                190, 189, 188, 187, 186, 185, 184, 183, 182, 181, 180,
                179, 178, 177, 176, 175, 174
            },
            st.getPaths()[5]
        );
        assertArrayEquals(
            new int[] {
                201, 200, 199, 198, 197,
                220, 221, 222, 223, 224,
                190, 189, 188, 187, 186,
                225, 226, 227, 228, 229,
                180, 179, 178, 177, 176, 175, 174
            },
            st.getPaths()[6]
        );
        assertArrayEquals(
            new int[] {
                197, 198, 199, 200, 201, 202, 203, 204, 205,
                219, 218, 217, 216, 215, 214, 213, 212, 211, 
                210, 209, 208, 207, 206, 205, 204, 203, 202, 
                201, 200, 199
            },
            st.getPaths()[7]
        );
    }

    @Test
    public void uploadGreenLine() throws IOException, FailedToReadPlc {
        st = new WCStaticTrack(new File("src/main/resources/wayside/green.plc"));

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
    public void uploadRedLine() throws IOException, FailedToReadPlc {
        st = new WCStaticTrack(new File("src/main/resources/wayside/red.plc"));

        assertEquals(230, st.trackLen());
        
        assertEquals(7, st.getSwitches().length);
        assertEquals(
            new WCSwitch( 8, 169, 168, 154 ),
            st.getSwitches()[0]
        );
        assertEquals(
            new WCSwitch( 9, 162, 163, 153 ),
            st.getSwitches()[1]
        );
        assertEquals(
            new WCSwitch( 3, 180, 181, 229 ),
            st.getSwitches()[2]
        );
        assertEquals(
            new WCSwitch( 4, 186, 185, 225 ),
            st.getSwitches()[3]
        );
        assertEquals(
            new WCSwitch( 5, 190, 191, 224 ),
            st.getSwitches()[4]
        );
        assertEquals(
            new WCSwitch( 6, 197, 196, 220 ),
            st.getSwitches()[5]
        );
        assertEquals(
            new WCSwitch( 7, 205, 206, 219 ),
            st.getSwitches()[6]
        );

        assertArrayEquals(
            new int[] {200},
            st.getCrossings()
        );

        assertArrayEquals(
            new int[] {
                176, 175, 174, 173, 172, 171, 170, 169, 168, 167, 166,
                165, 164, 163, 162, 161, 160, 159, 158, 157, 156, 155, 154
            },
            st.getPaths()[0]
        );
        assertArrayEquals(
            new int[] {
                153, 162, 161, 160, 159, 158, 157, 156, 155, 154, 185,
                184, 183, 182, 181, 180, 179, 178, 177, 176, 175, 174
            },
            st.getPaths()[1]
        );
        assertArrayEquals(
            new int[] {
                201, 200, 199, 198, 197, 196, 195, 194, 193, 192, 191,
                190, 189, 188, 187, 186, 185, 184, 183, 182, 181, 180,
                179, 178, 177, 176, 175, 174
            },
            st.getPaths()[2]
        );
        assertArrayEquals(
            new int[] {
                201, 200, 199, 198, 197,
                220, 221, 222, 223, 224,
                190, 189, 188, 187, 186,
                225, 226, 227, 228, 229,
                180, 179, 178, 177, 176, 175, 174
            },
            st.getPaths()[3]
        );
        assertArrayEquals(
            new int[] {
                197, 198, 199, 200, 201, 202, 203, 204, 205,
                219, 218, 217, 216, 215, 214, 213, 212, 211, 
                210, 209, 208, 207, 206, 205, 204, 203, 202, 
                201, 200, 199
            },
            st.getPaths()[4]
        );
    }
}