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
    public void setup() throws SQLException, ClassNotFoundException {

        // init track model
        _tm = TrackModel.init();

        // clear and create a blank database
        Statement stmt = _tm.conn.createStatement();
        stmt.execute("DELETE FROM blocks;");

        PreparedStatement _s = _tm.conn.prepareStatement("INSERT INTO blocks (id,region,grade,elevation,length,station,switch_root,switch_leaf,rr_crossing,line,next,bidirectional,speed_limit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

        // insert a few basic blocks
        int i = 1;
        _s.setInt(i++, 1); // id
        _s.setString(i++, "A"); // region
        _s.setDouble(i++, 0.5); // grade
        _s.setDouble(i++, 17.2); // elevation
        _s.setInt(i++, 50); // length
        _s.setString(i++, "STATION"); // station
        _s.setInt(i++, 1); // switch_root
        _s.setNull(i++, java.sql.Types.INTEGER); // switch_leaf
        _s.setInt(i++, 1); // rr_crossing
        _s.setString(i++, "GREEN"); // line
        _s.setInt(i++, 2); // next
        _s.setInt(i++, 1); // bidirectional
        _s.setInt(i++, 15); // speed_limit
        _s.executeUpdate();

        i = 1;
        _s.setInt(i++, 2); // id
        _s.setString(i++, "A"); // region
        _s.setDouble(i++, 0.5); // grade
        _s.setDouble(i++, 17); // elevation
        _s.setInt(i++, 50); // length
        _s.setString(i++, ""); // station
        _s.setNull(i++, java.sql.Types.INTEGER); // switch_root
        _s.setInt(i++, 1);  // switch_leaf
        _s.setInt(i++, 1); // rr_crossing
        _s.setString(i++, "GREEN"); // line
        _s.setInt(i++, 3); // next
        _s.setInt(i++, 1);  // bidirectional
        _s.setInt(i++, 15);  // speed_limit
        _s.executeUpdate();

        i = 1;
        _s.setInt(i++, 3); // id
        _s.setString(i++, "A"); // region
        _s.setDouble(i++, 0.5); // grade
        _s.setDouble(i++, 17); // elevation
        _s.setInt(i++, 50); // length
        _s.setString(i++, ""); // station
        _s.setNull(i++, java.sql.Types.INTEGER); // switch_root
        _s.setInt(i++, 1);  // switch_leaf
        _s.setInt(i++, 1); // rr_crossing
        _s.setString(i++, "GREEN"); // line
        _s.setInt(i++, 4); // next
        _s.setInt(i++, 1);  // bidirectional
        _s.setInt(i++, 15);  // speed_limit
        _s.executeUpdate();

        i = 1;
        _s.setInt(i++, 4); // id
        _s.setString(i++, "A"); // region
        _s.setDouble(i++, 0.5); // grade
        _s.setDouble(i++, 17); // elevation
        _s.setInt(i++, 50); // length
        _s.setString(i++, ""); // station
        _s.setNull(i++, java.sql.Types.INTEGER); // switch_root
        _s.setNull(i++, java.sql.Types.INTEGER); // switch_leaf
        _s.setInt(i++, 1);  // rr_crossing
        _s.setString(i++, "GREEN");  // line
        _s.setInt(i++, 1);  // next
        _s.setInt(i++, 1);  // bidirectional
        _s.setInt(i++, 15);  // speed_limit
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
    public void testTrackModelImport() throws IOException, SQLException {
        File file = new File(
            this.getClass().getClassLoader().getResource("TrackModel/track.csv").getFile());

        // function should return true
        assertTrue(TrackModel.importTrack(file));

        // database should now have csv data in it
        String row = "";
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
            _rs.getString("station") + "," +
            _rs.getString("switch_root") + "," +
            _rs.getString("switch_leaf") + "," +
            _rs.getString("next");

        // note: this string was constructed from the database; the export
        // doesn't have null strings in it
        assertEquals("1,A,0.5,17.2,50.0,STATION,null,null,2", row);
    }

    /**
     * Validate basic, typical use case export with a new file.
     */
    @Test
    public void testTrackModelExportNewFile() throws IOException, SQLException {
        File tempFolder = folder.newFolder("temp");
        File file = new File(tempFolder.getAbsolutePath() + "/track_export_new.csv");

        // function should return true
        assertTrue(TrackModel.exportTrack(file));

        // track_export.csv should now have csv data in it
        String header = "";
        String firstRow = "";
        String secondRow = "";
        String thirdRow = "";

        BufferedReader br = new BufferedReader(new FileReader(file));
        header = br.readLine();
        firstRow = br.readLine();
        secondRow = br.readLine();
        thirdRow = br.readLine();
        br.close();


        assertEquals("id,region,grade,elevation,length,station,switch_root,switch_leaf,rr_crossing,line,next,bidirectional,speed_limit", header);
        assertEquals("1,A,0.5,17.2,50.0,STATION,1,,1,GREEN,2,1,15", firstRow);
        assertEquals("2,A,0.5,17.0,50.0,,,1,1,GREEN,3,1,15", secondRow);
        assertEquals("3,A,0.5,17.0,50.0,,,1,1,GREEN,4,1,15", thirdRow);
    }

    /**
     * Validate export where file already exists.
     */
    @Test
    public void testTrackModelExportFileExists() throws IOException, SQLException  {
        File file = folder.newFile("track_export.csv");

        // function should return true
        assertTrue(TrackModel.exportTrack(file));

        // track_export.csv should now have csv data in it
        String header = "";
        String firstRow = "";
        String secondRow = "";
        String thirdRow = "";

        BufferedReader br = new BufferedReader(new FileReader(file));
        header = br.readLine();
        firstRow = br.readLine();
        secondRow = br.readLine();
        thirdRow = br.readLine();
        br.close();

        assertEquals("id,region,grade,elevation,length,station,switch_root,switch_leaf,rr_crossing,line,next,bidirectional,speed_limit", header);
        assertEquals("1,A,0.5,17.2,50.0,STATION,1,,1,GREEN,2,1,15", firstRow);
        assertEquals("2,A,0.5,17.0,50.0,,,1,1,GREEN,3,1,15", secondRow);
        assertEquals("3,A,0.5,17.0,50.0,,,1,1,GREEN,4,1,15", thirdRow);
    }

    /**
     * Validate basic occupancy check.
     */
    @Test
    public void testTrackModelisOccupied() throws SQLException {
        assertFalse(TrackModel.isOccupied(1));

        Statement stmt = _tm.conn.createStatement();
        stmt.execute("UPDATE blocks SET occupied = 1 WHERE id = 1;");

        assertTrue(TrackModel.isOccupied(1));
    }

    /**
     * Validate basic occupancy set.
     */
    @Test
    public void testTrackModelSetOccupiedTrue() throws SQLException {

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
    public void testTrackModelSetOccupiedFalse() throws SQLException {

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
    public void testTrackModelGetBlockIds() throws SQLException {
        ArrayList<String> ids = TrackModel.getBlockIds();

        assertEquals(4, ids.size());
        assertEquals("1", ids.get(0));
        assertEquals("2", ids.get(1));
        assertEquals("3", ids.get(2));
        assertEquals("4", ids.get(3));
    }

    /**
     * Validate getstaticblock works with a block that isn't a switch.
     */
    @Test
    public void testTrackModelGetStaticBlockNoSwitch() throws SQLException {
        StaticBlock block = TrackModel.getStaticBlock(4);

        assertNotNull(block);
        assertEquals(4, block.getId());
        assertEquals("A", block.getRegion());
        assertEquals(.5, block.getGrade(), epsilon);
        assertEquals(17, block.getElevation(), epsilon);
        assertEquals(50, block.getLength(), epsilon);
        assertEquals("", block.getStation());
        assertNull(block.getStaticSwitch());
    }

    /**
     * Validate getstaticblock works with a block that is the root of a switch.
     */
    @Test
    public void testTrackModelGetStaticBlockRootSwitch() throws SQLException {
        StaticBlock block = TrackModel.getStaticBlock(1);

        assertNotNull(block);
        assertEquals(1, block.getId());
        assertEquals("A", block.getRegion());
        assertEquals(.5, block.getGrade(), epsilon);
        assertEquals(17.2, block.getElevation(), epsilon);
        assertEquals(50, block.getLength(), epsilon);
        assertEquals("STATION", block.getStation());
        assertNotNull(block.getStaticSwitch());
    }

    /**
     * Validate getstaticblock works. Get a block that is an inactive leaf node
     * on its switch
     */
    @Test
    public void testTrackModelGetStaticBlockInactiveLeafSwitch() throws SQLException {
        StaticBlock block = TrackModel.getStaticBlock(2);

        assertNotNull(block);
        assertEquals(2, block.getId());
        assertEquals("A", block.getRegion());
        assertEquals(.5, block.getGrade(), epsilon);
        assertEquals(17, block.getElevation(), epsilon);
        assertEquals(50, block.getLength(), epsilon);
        assertEquals("", block.getStation());
        assertNotNull(block.getStaticSwitch());
    }

    /**
     * Validate getstaticblock works. Get a block that is an active leaf node on
     * its switch
     */
    @Test
    public void testTrackModelGetStaticBlockActiveLeafSwitch() throws SQLException {
        StaticBlock block = TrackModel.getStaticBlock(3);

        assertNotNull(block);
        assertEquals(3, block.getId());
        assertEquals("A", block.getRegion());
        assertEquals(.5, block.getGrade(), epsilon);
        assertEquals(17, block.getElevation(), epsilon);
        assertEquals(50, block.getLength(), epsilon);
        assertEquals("", block.getStation());
        assertNotNull(block.getStaticSwitch());
    }

    /**
     * Validate getstaticSwitch works.
     */
    @Test
    public void testTrackModelGetStaticSwitch() throws SQLException {
        StaticSwitch sw = TrackModel.getStaticSwitch(1);

        assertNotNull(sw);
        assertNotNull(sw.getRoot());
        assertNotNull(sw.getInactiveLeaf());
        assertNotNull(sw.getActiveLeaf());
    }

    /**
     * Validate basic region set.
     */
    @Test
    public void testTrackModelSetRegion() throws SQLException {

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
    public void testTrackModelSetLength() throws SQLException {
        assertTrue(TrackModel.setLength(1, 15.0));

        Double length = null;
        PreparedStatement stmt = _tm.conn.prepareStatement("SELECT length FROM blocks WHERE id = ?;");
        stmt.setInt(1, 1);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        length = rs.getDouble("length");

        assertEquals(length, 15.0, epsilon);
    }

    /**
     * Validate basic elevation set.
     */
    @Test
    public void testTrackModelSetElevation() throws SQLException {
        assertTrue(TrackModel.setElevation(1, 15.0));

        Double elevation = null;
        PreparedStatement stmt = _tm.conn.prepareStatement("SELECT elevation FROM blocks WHERE id = ?;");
        stmt.setInt(1, 1);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        elevation = rs.getDouble("elevation");

        assertEquals(elevation, 15.0, epsilon);
    }

    /**
     * Validate basic grade set.
     */
    @Test
    public void testTrackModelSetGrade() throws SQLException {
        assertTrue(TrackModel.setGrade(1, .75));

        PreparedStatement stmt = _tm.conn.prepareStatement("SELECT grade FROM blocks WHERE id = ?;");
        stmt.setInt(1, 1);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        Double grade = rs.getDouble("grade");

        assertEquals(grade, .75, epsilon);
    }

    /**
     * Validate basic switch set.
     */
    @Test
    public void testTrackModelSetSwitch() throws SQLException {
        assertTrue(TrackModel.setSwitch(1, true));

        PreparedStatement stmt = _tm.conn.prepareStatement("SELECT switch_active FROM blocks WHERE id = ?;");
        stmt.setInt(1, 1);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        int switch_active = rs.getInt("switch_active");

        assertTrue(switch_active > 0);
    }

    /**
     * Validate basic switch get.
     */
    @Test
    public void testTrackModelGetSwitch() throws SQLException {
        assertFalse(TrackModel.getSwitch(1));

        PreparedStatement stmt = _tm.conn.prepareStatement("UPDATE blocks SET switch_active = ? WHERE id = ?;");
        stmt.setInt(1, 1);
        stmt.setInt(2, 1);
        stmt.execute();

        assertTrue(TrackModel.getSwitch(1));
    }
}