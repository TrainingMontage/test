
package wayside;

import java.io.*;
import org.junit.*;

public class TestPlcImport {

    PlcImporter plc;

    public TestPlcImport() throws IOException {
        plc = new PlcImporter(new File("src/main/resources/wayside/track.plc"));
    }

    @Test
    public void trackLen() {
        Assert.assertEquals(153, plc.getTrackLen());
    }

    @Test
    public void switchesFromFile() {
        int[] expected = new int[] {1,2,10,11,12,13};
        Assert.assertArrayEquals(expected, plc.getSwitches());
    }

    @Test
    public void switchRoots() {
        int[] expected = new int[] {13,28,57,63,77,85};
        Assert.assertArrayEquals(expected, plc.getSwitchBlocks());
    }

    @Test
    public void switchDefault() {
        Assert.assertArrayEquals(
            new int[] {12,29,58,62,76,86},
            plc.getDefaultSwitches()
        );
    }

    @Test
    public void switchActive() {
        Assert.assertArrayEquals(
            new int[] {1,150,151,152,101,100},
            plc.getActiveSwitches()
        );
    }

    @Test
    public void everything() {
        Assert.assertEquals(153, plc.getTrackLen());
        Assert.assertArrayEquals(
            new int[] {1,2,10,11,12,13}, 
            plc.getSwitches());
        Assert.assertArrayEquals(
            new int[] {13,28,57,63,77,85}, 
            plc.getSwitchBlocks()
        );
        Assert.assertArrayEquals(
            new int[] {12,29,58,62,76,86},
            plc.getDefaultSwitches()
        );
        switchActive();
    }
}