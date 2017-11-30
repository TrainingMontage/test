
package wayside;

import java.io.*;
import java.util.Scanner;

public class PlcImporter {

    private int track_len;
    private int[] switches;

    public PlcImporter(File file) throws IOException {
        Scanner in = new Scanner(file);
        read_file: while (in.hasNextLine()) {
            String temp = in.nextLine();
            String[] line = temp.split("=");
            String lhs = line[0].trim();
            String rhs = "";
            if (line.length >= 2)
                rhs = line[1].trim();

            switch (lhs) {
                case "END": break read_file;
                case "TRACK_LEN":
                    track_len = Integer.parseInt(rhs);
                case "SWITCHES":
                    switches = buildArray(rhs);
            }
        }
    }

    private int[] buildArray(String arr) {
        String[] stuff = arr.split(",");
        int[] res = new int[stuff.length];
        for (int i = 0; i < stuff.length; i++) {
            res[i] = Integer.parseInt(stuff[i].trim());
        }
        return res;
    }

    public int getTrackLen() {
        return track_len;
    }

    public int[] getSwitches() {
        return switches;
    }
}