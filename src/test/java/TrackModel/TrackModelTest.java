package trackmodel;

import java.io.*;
import java.sql.*;
import java.util.*;

import org.junit.*;
import org.junit.rules.*;
import static org.junit.Assert.*;

public class TrackModelTest {
    public TrackModel _tm;
    public static double epsilon = 0.0001;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setup() throws Exception {

        // init track model
        _tm = TrackModel.init();

        // clear and create a blank database
        Statement stmt = _tm.conn.createStatement();
        stmt.execute("DELETE FROM blocks;");

        PreparedStatement _s = _tm.conn.prepareStatement("INSERT INTO blocks (id,region,grade,elevation,length,station) VALUES (?, ?, ?, ?, ?, ?);");
        int i = 1;
        _s.setInt(i++, 1);
        _s.setString(i++, "A");
        _s.setDouble(i++, 0.5);
        _s.setDouble(i++, 17.2);
        _s.setInt(i++, 50);
        _s.setString(i++, "STATION");
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
            row =
                _rs.getString("id") + "," + 
                _rs.getString("region") + "," + 
                _rs.getString("grade") + "," + 
                _rs.getString("elevation") + "," + 
                _rs.getString("length") + "," + 
                _rs.getString("station");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals("1,A,0.5,17.2,50.0,STATION", row);
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

        assertEquals("id,region,grade,elevation,length,station", header);
        assertEquals("1,A,0.5,17.2,50.0,STATION", firstRow);
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
    public void testTrackModelSetOccupiedTrue() {

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
     * Validate basic occupancy set.
     */
    @Test
    public void testTrackModelSetOccupiedFalse() {

        assertTrue(TrackModel.setOccupied(1, false));

        Integer occupied = 1;
        try {
            PreparedStatement stmt = _tm.conn.prepareStatement("SELECT occupied FROM blocks WHERE id = ?;");
            stmt.setInt(1, 1);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            occupied = (Integer) rs.getObject("occupied");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNull(occupied);
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

    /**
     * Validate getstaticblock works.
     */
    @Test
    public void testTrackModelGetStaticBlock() {
        StaticBlock block = TrackModel.getStaticBlock(1);

        assertEquals(block.getId(), 1);
        assertEquals(block.getRegion(), "A");
        assertEquals(block.getGrade(), .5, epsilon);
        assertEquals(block.getElevation(), 17.2, epsilon);
        assertEquals(block.getLength(), 50, epsilon);
        assertEquals(block.getStation(), "STATION");
    }

    /**
     * Validate basic region set.
     */
    @Test
    public void testTrackModelSetRegion() {

        assertTrue(TrackModel.setRegion(1, "B"));

        String region = null;
        try {
            PreparedStatement stmt = _tm.conn.prepareStatement("SELECT region FROM blocks WHERE id = ?;");
            stmt.setInt(1, 1);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            region = rs.getString("region");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(region, "B");
    }

    /**
     * Validate basic length set.
     */
    @Test
    public void testTrackModelSetLength() {
        assertTrue(TrackModel.setLength(1, 15.0));

        Double length = null;
        try {
            PreparedStatement stmt = _tm.conn.prepareStatement("SELECT length FROM blocks WHERE id = ?;");
            stmt.setInt(1, 1);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            length = rs.getDouble("length");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(length, 15.0, epsilon);
    }

    /**
     * Validate basic elevation set.
     */
    @Test
    public void testTrackModelSetElevation() {
        assertTrue(TrackModel.setElevation(1, 15.0));

        Double elevation = null;
        try {
            PreparedStatement stmt = _tm.conn.prepareStatement("SELECT elevation FROM blocks WHERE id = ?;");
            stmt.setInt(1, 1);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            elevation = rs.getDouble("elevation");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(elevation, 15.0, epsilon);
    }

    /**
     * Validate basic grade set.
     */
    @Test
    public void testTrackModelSetGrade() {
        assertTrue(TrackModel.setGrade(1, .75));

        Double grade = null;
        try {
            PreparedStatement stmt = _tm.conn.prepareStatement("SELECT grade FROM blocks WHERE id = ?;");
            stmt.setInt(1, 1);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            grade = rs.getDouble("grade");
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(grade, .75, epsilon);
    }
}