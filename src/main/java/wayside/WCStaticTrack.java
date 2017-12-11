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

import java.io.File;
import java.io.IOException;

public class WCStaticTrack {

    private int TRACK_LEN = 9;
    private WCSwitch[] SWITCHES = new WCSwitch[] {
        new WCSwitch(1, 2, 3, 8)
    };
    private int[] CROSSINGS = new int[] {};
    private int[][] PATHS = new int[][] {
        new int[] {1,2,3,4,5,6,7,8},
        new int[] {3,4,5,6,7,8,2,1}
    };

    public static final String WHOLE_TRACK = "src/main/resources/wayside/track.plc";
    public static final String GREEN_LINE = "src/main/resources/wayside/green.plc";
    public static final String RED_LINE = "src/main/resources/wayside/red.plc";

    public WCStaticTrack(File file) throws IOException, FailedToReadPlc {
        PlcImporter plc = new PlcImporter(file);
        TRACK_LEN = plc.getTrackLen();
        SWITCHES = buildSwitches(plc);
        CROSSINGS = plc.getCrossings();
        PATHS = plc.getPaths();
    }

    private WCSwitch[] buildSwitches(PlcImporter plc) {
        int n = plc.getSwitches().length;
        WCSwitch[] sws = new WCSwitch[n];
        for (int i = 0; i < n; i++) {
            sws[i] = new WCSwitch(
                plc.getSwitches()[i],
                plc.getSwitchBlocks()[i],
                plc.getDefaultSwitches()[i],
                plc.getActiveSwitches()[i]
            );
        }
        return sws;
    }

    public WCStaticTrack(boolean greenLine) {
        if (!greenLine) return;
        int INTO_YARD = 151;
        int FROM_YARD = 152;
        TRACK_LEN = 153;
        SWITCHES = new WCSwitch[] {
            new WCSwitch( 1, 13, 12,   1),
            new WCSwitch( 2, 28, 29, 150),
            new WCSwitch(10, 57, 58, 151),
            new WCSwitch(11, 63, 62, 152),
            new WCSwitch(12, 77, 76, 101),
            new WCSwitch(13, 85, 86, 100)
        };
        CROSSINGS = new int[] {19};
        PATHS = new int[][] {
            // The long circuit around the entire track.
            new int[] {
                // high-C downto low-A,
                12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1,
                // low-D upto high-Q,
                13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28,
                29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44,
                45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60,
                61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76,
                77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92,
                93, 94, 95, 96, 97, 98, 99, 100,
                // high-N downto low-N,
                85, 84, 83, 82, 81, 80, 79, 78, 77,
                // low-R upto high-Z
                101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113,
                114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125, 126,
                127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139,
                140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150
            },
            // Leaving the yard, entering the track.
            new int[] {
                36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51,
                52, 53, 54, 55, 56, 57, INTO_YARD
            },
            // Entering the yard from the track.
            new int[] {
                FROM_YARD, 63, 64, 65, 66, 67, 68
            }
        };
    }

    public int trackLen() {
        return TRACK_LEN;
    }

    public WCSwitch[] getSwitches() {
        return SWITCHES;
    }

    public int[] getCrossings() {
        return CROSSINGS;
    }

    public int[][] getPaths() {
        return PATHS;
    }

}