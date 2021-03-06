
package wayside;

import java.io.*;
import org.junit.*;

public class TestPlcImport {

    PlcImporter plc;

    public TestPlcImport() throws IOException, FailedToReadPlc {
        plc = new PlcImporter(new File("src/main/resources/wayside/track.plc"));
    }

    @Test
    public void invalidPlc() throws IOException {
        try {
            plc = new PlcImporter(new File("src/main/resources/TrackModel/green.csv"));
            Assert.fail("Did not find invalid PLC as such");
        } catch (FailedToReadPlc e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void trackLen() {
        Assert.assertEquals(230, plc.getTrackLen());
    }

    @Test
    public void switchesFromFile() {
        int[] expected = new int[] {1,2,10,11,12,13,8,9,3,4,5,6,7};
        Assert.assertArrayEquals(expected, plc.getSwitches());
    }

    @Test
    public void switchRoots() {
        int[] expected = new int[] {13,28,57,63,77,85,169,162,180,186,190,197,205};
        Assert.assertArrayEquals(expected, plc.getSwitchBlocks());
    }

    @Test
    public void switchDefault() {
        Assert.assertArrayEquals(
            new int[] {12,29,58,62,76,86,168,163,181,185,191,196,206},
            plc.getDefaultSwitches()
        );
    }

    @Test
    public void switchActive() {
        Assert.assertArrayEquals(
            new int[] {1,150,151,152,101,100,154,153,229,225,224,220,219},
            plc.getActiveSwitches()
        );
    }

    @Test
    public void crossings() {
        Assert.assertArrayEquals(
            new int[] {19, 200},
            plc.getCrossings()
        );
    }

    @Test
    public void getFirstPath() {
        Assert.assertArrayEquals(
            new int[] {152, 63, 64, 65, 66, 67, 68},
            plc.getPaths()[2]
        );
    }

    @Test
    public void getSecondPath() {
        Assert.assertArrayEquals(
            new int[] {36, 37, 38, 39, 40, 41, 42, 43, 
            44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 151},
            plc.getPaths()[1]
        );
    }

    @Test
    public void getThirdPath() {
        Assert.assertArrayEquals(
            new int[] {
                12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 13, 14, 15, 16, 17, 18,
                19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34,
                35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
                51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66,
                67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82,
                83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98,
                99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111,
                112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124,
                125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137,
                138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150
            },
            plc.getPaths()[0]
        );
    }

    @Test
    public void everything() {
        trackLen();
        switchesFromFile();
        switchRoots();
        switchDefault();
        switchActive();
        crossings();
        getFirstPath();
        getSecondPath();
        getThirdPath();
    }
}