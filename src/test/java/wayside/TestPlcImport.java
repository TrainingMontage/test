
package wayside;

import java.io.*;
import org.junit.*;

public class TestPlcImport {

    @Test
    public void constructs() throws IOException {
        PlcImporter plc = new PlcImporter(new File("src/main/resources/wayside/track.plc"));
    }

}