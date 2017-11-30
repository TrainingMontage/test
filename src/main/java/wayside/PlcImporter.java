
package wayside;

import java.io.*;
import java.util.Scanner;

public class PlcImporter {

    public PlcImporter(File file) throws IOException {
        Scanner in = new Scanner(file);
        while (in.hasNextLine()) {
            String temp = in.nextLine();
        }
    }
}