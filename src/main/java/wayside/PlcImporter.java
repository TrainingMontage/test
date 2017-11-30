
package wayside;

import java.io.*;
import java.util.Scanner;

public class PlcImporter {

    private int track_len;

    public PlcImporter(File file) throws IOException {
        Scanner in = new Scanner(file);
        read_file: while (in.hasNextLine()) {
            String temp = in.nextLine();
            String[] line = temp.split("=");
            switch (line[0].trim()) {
                case "END": break read_file;
                case "TRACK_LEN":
                    track_len = Integer.parseInt(line[1].trim());
            }
        }
    }

    public int getTrackLen() {
        return 153;
    }
}