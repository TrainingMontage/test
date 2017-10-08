package TrackModel;

import java.io.*;

import org.junit.*;
import static org.junit.Assert.*;

public class TrackModelTest {
    TrackModel _tm;

    @Before
    public void setup() {
        _tm = TrackModel.init();
    }

    /**
     * Make sure init actually creates an object
     */
    @Test
    public void testTrackModelInit() {
        assertNotNull(_tm);
    }

     /**
     * Make sure init actually creates the database
     */
    @Test
    public void testTrackModelInitDB() {
        assertNotNull(_tm.conn);
    }

    /**
     * Basic typical use case import test.
     */
    @Test
    public void testTrackModelImport() {
        File file = new File(this.getClass().getClassLoader().getResource("TrackModel/track.csv").getFile());
        assertTrue(TrackModel.importTrack(file));
    }
}