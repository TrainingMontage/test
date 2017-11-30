
package wayside;

import java.io.*;
import org.junit.*;

public class TestPlcImport {

    PlcImporter plc;

    public TestPlcImport() throws IOException {
        plc = new PlcImporter(new File("src/main/resources/wayside/track.plc"));
    }

    @Test
    public void trackLen() throws IOException {
        Assert.assertEquals(153, plc.getTrackLen());
    }
}