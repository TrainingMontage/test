package trackmodel;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.junit.*;
import org.junit.rules.*;
import static org.junit.Assert.*;

public class TrackModelTest {
    public TrackModel _tm;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setup() throws Exception {

        // init track model
        _tm = TrackModel.init();

        // clear and create a blank database
        Statement stmt = _tm.conn.createStatement();
        stmt.execute("DELETE FROM blocks;");

        PreparedStatement _s = _tm.conn.prepareStatement("INSERT INTO blocks (id,region,length,station) VALUES (?, ?, ?, ?);");
        _s.setInt(1, 1);
        _s.setString(2, "A");
        _s.setInt(3, 50);
        _s.setString(4, "STATION");
        _s.executeUpdate();

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
     * Validate basic, typical use case import.
     */
    @Test
    public void testTrackModelImport() {
        File file = new File(this.getClass().getClassLoader().getResource("TrackModel/track.csv").getFile());

        // function should return true
        assertTrue(TrackModel.importTrack(file));

        // database should now have csv data in it
        String row = "";
        try {
            Statement _s = _tm.conn.createStatement();
            ResultSet _rs = _s.executeQuery("SELECT * FROM blocks");
            _rs.next();

            // extract the first row
            row = _rs.getString("id") + "," + _rs.getString("region") + "," + _rs.getString("length") + "," + _rs.getString("station");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals("1,A,50,STATION", row);
    }

    /**
     * Validate basic, typical use case export.
     */
    @Test
    public void testTrackModelExport() throws IOException {
        File file = folder.newFile("myfile.txt");
        // File file = new File("track_export.csv");

        // function should return true
        assertTrue(TrackModel.exportTrack(file));

        // track_export.csv should now have csv data in it
        String header = "";
        String firstRow = "";

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            header = br.readLine();
            firstRow = br.readLine();
            br.close();

        } catch (Exception e) {
            System.err.println("Exception occurred" + e);
            e.printStackTrace();
            fail();
        }

        assertEquals("id,region,length,station", header);
        assertEquals("1,A,50,STATION", firstRow);
    }

    /**
     * Validate basic occupancy check.
     */
    @Test
    public void testTrackModelisOccupied() {
        assertFalse(TrackModel.isOccupied(1));

        try {
            Statement stmt = _tm.conn.createStatement();
            stmt.execute("UPDATE blocks SET occupied = 1 WHERE id = 1;");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertTrue(TrackModel.isOccupied(1));
    }

    /**
     * Validate basic occupancy set.
     */
    @Test
    public void testTrackModelSetOccupied() {

        assertTrue(TrackModel.setOccupied(1, true));

        Integer occupied = null;
        try {
            PreparedStatement stmt = _tm.conn.prepareStatement("SELECT occupied FROM blocks WHERE id = ?;");
            stmt.setInt(1, 1);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            occupied = (Integer) rs.getObject("occupied");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(occupied, (Integer) 1);
    }

    /**
     * Validate basic getblocks call.
     */
    @Test
    public void testTrackModelGetBlockIds() {
        ArrayList<String> ids = TrackModel.getBlockIds();

        assertEquals(1, ids.size());
        assertEquals("1", ids.get(0));
    }
}