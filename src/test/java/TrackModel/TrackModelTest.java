/*   ______                 _           _
 *  /_  __/ _____  ____ _  (_) ____    (_) ____    ____ _
 *   / /   / ___/ / __ `/ / / / __ \  / / / __ \  / __ `/
 *  / /   / /    / /_/ / / / / / / / / / / / / / / /_/ /
 * /_/   /_/     \__,_/ /_/ /_/ /_/ /_/ /_/ /_/  \__, /
 *     __  ___                 __               /____/
 *    /  |/  / ____    ____   / /_  ____ _  ____ _  ___
 *   / /|_/ / / __ \  / __ \ / __/ / __ `/ / __ `/ / _ \
 *  / /  / / / /_/ / / / / // /_  / /_/ / / /_/ / /  __/
 * /_/  /_/  \____/ /_/ /_/ \__/  \__,_/  \__, /  \___/
 *                                       /____/
 *
 * @author Alec Rosenbaum
 */

package trackmodel;

import java.io.*;
import java.sql.*;
import java.util.*;
import shared.*;

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

        PreparedStatement _s = _tm.conn.prepareStatement("INSERT INTO blocks (id,region,grade,elevation,length,station,switch_root,switch_leaf,rr_crossing,underground,line,next,bidirectional,speed_limit,beacon,heater) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

        // insert a few basic blocks
        //        _< 3-> < 4-> <-
        //       /              5
        // < 1-> - <-2 > < 6-> <
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
        _s.setInt(i++, 0); // underground
        _s.setString(i++, "GREEN"); // line
        _s.setInt(i++, 2); // next
        _s.setInt(i++, 1); // bidirectional
        _s.setInt(i++, 15); // speed_limit
        _s.setNull(i++, java.sql.Types.INTEGER); // beacon
        _s.setInt(i++, 1); // heater
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
        _s.setInt(i++, 0); // underground
        _s.setString(i++, "GREEN"); // line
        _s.setInt(i++, 1); // next
        _s.setInt(i++, 1);  // bidirectional
        _s.setInt(i++, 15);  // speed_limit
        _s.setNull(i++, java.sql.Types.INTEGER); // beacon
        _s.setInt(i++, 1); // heater
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
        _s.setInt(i++, 0); // underground
        _s.setString(i++, "GREEN"); // line
        _s.setInt(i++, 4); // next
        _s.setInt(i++, 1);  // bidirectional
        _s.setInt(i++, 15);  // speed_limit
        _s.setNull(i++, java.sql.Types.INTEGER); // beacon
        _s.setInt(i++, 0); // heater
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
        _s.setInt(i++, 1); // rr_crossing
        _s.setInt(i++, 0); // underground
        _s.setString(i++, "GREEN");  // line
        _s.setInt(i++, 5);  // next
        _s.setInt(i++, 1);  // bidirectional
        _s.setInt(i++, 15);  // speed_limit
        _s.setNull(i++, java.sql.Types.INTEGER); // beacon
        _s.setInt(i++, 0); // heater
        _s.executeUpdate();

        i = 1;
        _s.setInt(i++, 5); // id
        _s.setString(i++, "A"); // region
        _s.setDouble(i++, 0.5); // grade
        _s.setDouble(i++, 17); // elevation
        _s.setInt(i++, 50); // length
        _s.setString(i++, ""); // station
        _s.setNull(i++, java.sql.Types.INTEGER); // switch_root
        _s.setNull(i++, java.sql.Types.INTEGER); // switch_leaf
        _s.setInt(i++, 1); // rr_crossing
        _s.setInt(i++, 0); // underground
        _s.setString(i++, "GREEN");  // line
        _s.setInt(i++, 4);  // next
        _s.setInt(i++, 1);  // bidirectional
        _s.setInt(i++, 15);  // speed_limit
        _s.setNull(i++, java.sql.Types.INTEGER); // beacon
        _s.setInt(i++, 0); // heater
        _s.executeUpdate();

        i = 1;
        _s.setInt(i++, 6); // id
        _s.setString(i++, "A"); // region
        _s.setDouble(i++, 0.5); // grade
        _s.setDouble(i++, 17); // elevation
        _s.setInt(i++, 50); // length
        _s.setString(i++, ""); // station
        _s.setNull(i++, java.sql.Types.INTEGER); // switch_root
        _s.setNull(i++, java.sql.Types.INTEGER); // switch_leaf
        _s.setInt(i++, 1); // rr_crossing
        _s.setInt(i++, 0); // underground
        _s.setString(i++, "GREEN");  // line
        _s.setInt(i++, 5);  // next
        _s.setInt(i++, 1);  // bidirectional
        _s.setInt(i++, 15);  // speed_limit
        _s.setNull(i++, java.sql.Types.INTEGER); // beacon
        _s.setInt(i++, 0); // heater
        _s.executeUpdate();

        // add a train
        stmt.execute("DELETE FROM trains;");

        _s = _tm.conn.prepareStatement("INSERT INTO trains (id,curr_block,position,direction,reported_change,reported_passengers,loaded_passengers) VALUES (?, ?, ?, ?, ?, ?, ?);");

        i = 1;
        _s.setInt(i++, 1); // id
        _s.setInt(i++, 1); // curr_block
        _s.setDouble(i++, 0.0); // position
        _s.setInt(i++, 0); // direction
        _s.setInt(i++, 0); // reported_change
        _s.setInt(i++, 0); // reported_passengers
        _s.setInt(i++, 0); // loaded_passengers
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
     * Make sure initWithTestData actually creates an object and populates some
     * test data
     */
    @Test
    public void testTrackModelInitWithTestData() throws IOException, SQLException, ClassNotFoundException {
        _tm = _tm.initWithTestData();

        assertNotNull(_tm);

        // this is not the best way to test this, as it relies on other methods
        // within the class
        ArrayList<Integer> ids = _tm.getBlockIds();
        assertEquals(8, ids.size());
    }

    /**
     * Validate basic, typical use case import.
     */
    @Test
    public void testTrackModelImport() throws IOException, SQLException {
        File file = new File(
            this.getClass().getClassLoader().getResource("TrackModel/track.csv").getFile());

        // function should return true
        assertTrue(_tm.importTrack(file));

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
            _rs.getString("rr_crossing") + "," +
            _rs.getString("underground") + "," +
            _rs.getString("line") + "," +
            _rs.getString("next") + "," +
            _rs.getString("bidirectional") + "," +
            _rs.getString("speed_limit") + "," +
            _rs.getString("beacon") + "," +
            _rs.getString("heater");

        // note: this string was constructed from the database; the export
        // doesn't have null strings in it
        assertEquals("1,A,0.5,17.2,50.0,STATION,null,null,0,0,GREEN,2,1,15,null,0", row);
    }

    /**
     * Validate green line import.
     */
    @Test
    public void testTrackModelImportGreenLine() throws IOException, SQLException {
        File file = new File(
            this.getClass().getClassLoader().getResource("TrackModel/green.csv").getFile());

        // function should return true
        assertTrue(_tm.importTrack(file));

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
            _rs.getString("rr_crossing") + "," +
            _rs.getString("underground") + "," +
            _rs.getString("line") + "," +
            _rs.getString("next") + "," +
            _rs.getString("bidirectional") + "," +
            _rs.getString("speed_limit") + "," +
            _rs.getString("beacon") + "," +
            _rs.getString("heater");

        // note: this string was constructed from the database; the export
        // doesn't have null strings in it
        assertEquals("1,A,0.5,0.5,100.0,,null,1,0,0,GREEN,2,0,15.27779,null,1", row);
    }

    /**
     * Validate basic, typical use case export with a new file.
     */
    @Test
    public void testTrackModelExportNewFile() throws IOException, SQLException {
        File tempFolder = folder.newFolder("temp");
        File file = new File(tempFolder.getAbsolutePath() + "/track_export_new.csv");

        // function should return true
        assertTrue(_tm.exportTrack(file));

        // track_export.csv should now have csv data in it
        String firstRow = "";
        String secondRow = "";
        String thirdRow = "";

        BufferedReader br = new BufferedReader(new FileReader(file));
        firstRow = br.readLine();
        secondRow = br.readLine();
        thirdRow = br.readLine();
        br.close();

        assertEquals("1,A,0.5,17.2,50.0,STATION,1,,1,0,GREEN,2,1,15,,1", firstRow);
        assertEquals("2,A,0.5,17.0,50.0,,,1,1,0,GREEN,1,1,15,,1", secondRow);
        assertEquals("3,A,0.5,17.0,50.0,,,1,1,0,GREEN,4,1,15,,0", thirdRow);
    }

    /**
     * Validate export where file already exists.
     */
    @Test
    public void testTrackModelExportFileExists() throws IOException, SQLException  {
        File file = folder.newFile("track_export.csv");

        // function should return true
        assertTrue(_tm.exportTrack(file));

        // track_export.csv should now have csv data in it
        String firstRow = "";
        String secondRow = "";
        String thirdRow = "";

        BufferedReader br = new BufferedReader(new FileReader(file));
        firstRow = br.readLine();
        secondRow = br.readLine();
        thirdRow = br.readLine();
        br.close();

        assertEquals("1,A,0.5,17.2,50.0,STATION,1,,1,0,GREEN,2,1,15,,1", firstRow);
        assertEquals("2,A,0.5,17.0,50.0,,,1,1,0,GREEN,1,1,15,,1", secondRow);
        assertEquals("3,A,0.5,17.0,50.0,,,1,1,0,GREEN,4,1,15,,0", thirdRow);
    }

    /**
     * Validate basic occupancy check.
     */
    @Test
    public void testTrackModelisOccupied() throws SQLException {
        assertFalse(_tm.isOccupied(1));

        Statement stmt = _tm.conn.createStatement();
        stmt.execute("UPDATE blocks SET occupied = 1 WHERE id = 1;");

        assertTrue(_tm.isOccupied(1));
    }

    /**
     * Validate basic occupancy set.
     */
    @Test
    public void testTrackModelSetOccupiedTrue() throws SQLException {

        assertTrue(_tm.setOccupied(1, true));

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

        assertFalse(_tm.setOccupied(1, false));

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
        ArrayList<Integer> ids = _tm.getBlockIds();

        assertEquals(6, ids.size());
        assertEquals(1, (int) ids.get(0));
        assertEquals(2, (int) ids.get(1));
        assertEquals(3, (int) ids.get(2));
        assertEquals(4, (int) ids.get(3));
        assertEquals(5, (int) ids.get(4));
        assertEquals(6, (int) ids.get(5));
    }

    /**
     * Validate basic getswitchids call.
     */
    @Test
    public void testTrackModelGetSwitchIds() throws SQLException {
        ArrayList<Integer> ids = _tm.getSwitchIds();

        assertEquals(1, ids.size());
        assertEquals(1, (int) ids.get(0));
    }

    /**
     * Validate getstaticblock works with a block that isn't a switch.
     */
    @Test
    public void testTrackModelGetStaticBlockNoSwitch() throws SQLException {
        StaticBlock block = _tm.getStaticBlock(4);

        assertNotNull(block);
        assertEquals(4, block.getId());
        assertEquals("A", block.getRegion());
        assertEquals(.5, block.getGrade(), epsilon);
        assertEquals(17, block.getElevation(), epsilon);
        assertEquals(50, block.getLength(), epsilon);
        assertEquals(15, block.getSpeedLimit(), epsilon);
        assertEquals("", block.getStation());
        assertEquals("GREEN", block.getLine());
        assertTrue(block.isCrossing());
        assertFalse(block.isUnderground());
        assertTrue(block.isBidirectional());
        assertFalse(block.hasHeater());
        assertEquals(5, block.getNextId());
        assertEquals(3, block.getPreviousId());
        assertNull(block.getStaticSwitch());
    }

    /**
     * Validate getstaticblock works with a block that is the root of a switch.
     */
    @Test
    public void testTrackModelGetStaticBlockRootSwitch() throws SQLException {
        StaticBlock block = _tm.getStaticBlock(1);

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
        StaticBlock block = _tm.getStaticBlock(2);

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
        StaticBlock block = _tm.getStaticBlock(3);

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
        StaticSwitch sw = _tm.getStaticSwitch(1);

        assertNotNull(sw);
        assertNotNull(sw.getRoot());
        assertNotNull(sw.getDefaultLeaf());
        assertNotNull(sw.getActiveLeaf());
    }

    /**
     * Validate basic region set.
     */
    @Test
    public void testTrackModelSetRegion() throws SQLException {

        assertTrue(_tm.setRegion(1, "B"));

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
        assertTrue(_tm.setLength(1, 15.0));

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
        assertTrue(_tm.setElevation(1, 15.0));

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
        assertTrue(_tm.setGrade(1, .75));

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
    public void testTrackModelSetSwitchActive() throws SQLException {
        assertTrue(_tm.setSwitch(1, true));

        PreparedStatement stmt = _tm.conn.prepareStatement("SELECT switch_active FROM blocks WHERE id = ?;");
        stmt.setInt(1, 1);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        int switch_active = rs.getInt("switch_active");

        assertTrue(switch_active > 0);
    }

    /**
     * Validate basic switch set.
     */
    @Test
    public void testTrackModelSetSwitchFalse() throws SQLException {
        assertFalse(_tm.setSwitch(1, false));

        PreparedStatement stmt = _tm.conn.prepareStatement("SELECT switch_active FROM blocks WHERE id = ?;");
        stmt.setInt(1, 1);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        int switch_active = rs.getInt("switch_active");

        assertEquals(0, switch_active);
    }

    /**
     * Validate basic switch get.
     */
    @Test
    public void testTrackModelGetSwitch() throws SQLException {
        assertFalse(_tm.getSwitch(1));

        PreparedStatement stmt = _tm.conn.prepareStatement("UPDATE blocks SET switch_active = ? WHERE id = ?;");
        stmt.setInt(1, 1);
        stmt.setInt(2, 1);
        stmt.execute();

        assertTrue(_tm.getSwitch(1));
    }

    /**
     * Validate basic authority set.
     */
    @Test
    public void testTrackModelSetAuthorityTrue() throws SQLException {
        assertTrue(_tm.setAuthority(1, true));

        PreparedStatement stmt = _tm.conn.prepareStatement("SELECT authority FROM blocks WHERE id = ?;");
        stmt.setInt(1, 1);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        int authority = rs.getInt("authority");

        assertTrue(authority > 0);
    }

    /**
     * Validate basic authority set.
     */
    @Test
    public void testTrackModelSetAuthorityFalse() throws SQLException {
        assertFalse(_tm.setAuthority(1, false));

        PreparedStatement stmt = _tm.conn.prepareStatement("SELECT authority FROM blocks WHERE id = ?;");
        stmt.setInt(1, 1);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        int authority = rs.getInt("authority");

        assertEquals(0, authority);
    }

    /**
     * Validate basic speed set.
     */
    @Test
    public void testTrackModelSetSpeed() throws SQLException {
        assertEquals(14, _tm.setSpeed(1, 14));

        PreparedStatement stmt = _tm.conn.prepareStatement("SELECT speed FROM blocks WHERE id = ?;");
        stmt.setInt(1, 1);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        int speed = rs.getInt("speed");

        assertEquals(14, speed);
    }

    /**
     * Validate basic rr crossing status set.
     */
    @Test
    public void testTrackModelSetCrossingStateTrue() throws SQLException {
        assertTrue(_tm.setCrossingState(1, true));

        PreparedStatement stmt = _tm.conn.prepareStatement("SELECT crossing_active FROM blocks WHERE id = ?;");
        stmt.setInt(1, 1);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        int crossing_active = rs.getInt("crossing_active");

        assertTrue(crossing_active > 0);
    }

    /**
     * Validate basic rr crossing State set.
     */
    @Test
    public void testTrackModelSetCrossingStateFalse() throws SQLException {
        assertFalse(_tm.setCrossingState(1, false));

        PreparedStatement stmt = _tm.conn.prepareStatement("SELECT crossing_active FROM blocks WHERE id = ?;");
        stmt.setInt(1, 1);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        int crossing_active = rs.getInt("crossing_active");

        assertEquals(0, crossing_active);
    }

    /**
     * Validate basic rr crossing State get.
     */
    @Test
    public void testTrackModelGetCrossingState() throws SQLException {
        assertFalse(_tm.getCrossingState(1));

        PreparedStatement stmt = _tm.conn.prepareStatement("UPDATE blocks SET crossing_active = ? WHERE id = ?;");
        stmt.setInt(1, 1);
        stmt.setInt(2, 1);
        stmt.execute();

        assertTrue(_tm.getCrossingState(1));
    }


    /**
     * Validate basic Signal set.
     */
    @Test
    public void testTrackModelSetSignalTrue() throws SQLException {
        assertTrue(_tm.setSignal(1, true));

        PreparedStatement stmt = _tm.conn.prepareStatement("SELECT signal FROM blocks WHERE id = ?;");
        stmt.setInt(1, 1);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        int signal = rs.getInt("signal");

        assertTrue(signal > 0);
    }

    /**
     * Validate basic Signal set.
     */
    @Test
    public void testTrackModelSetSignalFalse() throws SQLException {
        assertFalse(_tm.setSignal(1, false));

        PreparedStatement stmt = _tm.conn.prepareStatement("SELECT signal FROM blocks WHERE id = ?;");
        stmt.setInt(1, 1);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        int signal = rs.getInt("signal");

        assertEquals(0, signal);
    }

    /**
     * Validate basic Signal get.
     */
    @Test
    public void testTrackModelGetSignal() throws SQLException {
        assertFalse(_tm.getSignal(1));

        PreparedStatement stmt = _tm.conn.prepareStatement("UPDATE blocks SET signal = ? WHERE id = ?;");
        stmt.setInt(1, 1);
        stmt.setInt(2, 1);
        stmt.execute();

        assertTrue(_tm.getSignal(1));
    }

    /**
     * Validate basic train initialization.
     */
    @Test
    public void testTrackModelInitializeTrain() throws SQLException {
        assertTrue(_tm.initializeTrain(2, 1));

        PreparedStatement stmt = _tm.conn.prepareStatement("SELECT curr_block FROM trains WHERE id = ?;");
        stmt.setInt(1, 1);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        int curr_block = rs.getInt("curr_block");

        assertEquals(1, curr_block);
    }

    /**
     * Validate basic train authority get.
     */
    @Test
    public void testTrackModelGetTrainAuthority() throws SQLException {
        assertEquals(false, _tm.getTrainAuthority(1));

        PreparedStatement stmt = _tm.conn.prepareStatement("UPDATE blocks SET authority = ? WHERE id = ?;");
        stmt.setInt(1, 1);
        stmt.setInt(2, 1);
        stmt.execute();

        assertEquals(true, _tm.getTrainAuthority(1));
    }

    /**
     * Validate basic train speed get.
     */
    @Test
    public void testTrackModelGetTrainSpeed() throws SQLException {
        assertEquals(0, _tm.getTrainSpeed(1), epsilon);

        PreparedStatement stmt = _tm.conn.prepareStatement("UPDATE blocks SET speed = ? WHERE id = ?;");
        stmt.setInt(1, 1);
        stmt.setInt(2, 1);
        stmt.execute();

        assertEquals(1, _tm.getTrainSpeed(1), epsilon);
    }

    /**
     * Validate basic train beacon get.
     */
    @Test
    public void testTrackModelGetTrainBeacon() throws SQLException {
        assertNull(_tm.getTrainBeacon(1));

        PreparedStatement stmt = _tm.conn.prepareStatement("UPDATE blocks SET beacon = ? WHERE id = ?;");
        stmt.setInt(1, 0);
        stmt.setInt(2, 1);
        stmt.execute();

        byte[] beacon = _tm.getTrainBeacon(1);

        assertEquals(4, beacon.length);
        assertEquals((byte) 0, beacon[0]);
        assertEquals((byte) 0, beacon[1]);
        assertEquals((byte) 0, beacon[2]);
        assertEquals((byte) 0, beacon[3]);
    }

    /**
     * Validate basic isTrackIcy call.
     */
    @Test
    public void testTrackModelIsIcyTrack() throws SQLException {
        assertFalse(_tm.isIcyTrack(1));

        Environment.temperature = TrackModel.FREEZING - 1;

        assertFalse(_tm.isIcyTrack(1));

        PreparedStatement stmt = _tm.conn.prepareStatement("UPDATE trains SET curr_block = ? WHERE id = ?;");
        stmt.setInt(1, 3);
        stmt.setInt(2, 1);
        stmt.execute();

        assertTrue(_tm.isIcyTrack(1));
    }

    /**
     * Validate basic train get grade.
     */
    @Test
    public void testTrackModelGetGrade() throws SQLException {
        assertEquals(0.5, _tm.getGrade(1), epsilon);
    }

    /**
    * Validate basic block status get.
    */
    @Test
    public void testTrackModelGetStatus() throws SQLException {
        assertEquals(BlockStatus.OPERATIONAL, _tm.getStatus(1));

        PreparedStatement stmt = _tm.conn.prepareStatement("UPDATE blocks SET status = ? WHERE id = ?;");
        stmt.setInt(1, BlockStatus.BROKEN.ordinal());
        stmt.setInt(2, 1);
        stmt.execute();

        assertEquals(BlockStatus.BROKEN, _tm.getStatus(1));
    }

    /**
    * Validate basic block status set.
    */
    @Test
    public void testTrackModelSetStatusOperational() throws SQLException {
        assertEquals(BlockStatus.OPERATIONAL, _tm.setStatus(1, BlockStatus.OPERATIONAL));

        PreparedStatement stmt = _tm.conn.prepareStatement("SELECT status FROM blocks WHERE id = ?;");
        stmt.setInt(1, 1);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        int status = rs.getInt("status");

        assertEquals(BlockStatus.OPERATIONAL.ordinal(), status);
    }

    /**
    * Validate basic block status set.
    */
    @Test
    public void testTrackModelSetStatusBroken() throws SQLException {
        assertEquals(BlockStatus.BROKEN, _tm.setStatus(1, BlockStatus.BROKEN));

        PreparedStatement stmt = _tm.conn.prepareStatement("SELECT status FROM blocks WHERE id = ?;");
        stmt.setInt(1, 1);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        int status = rs.getInt("status");

        assertEquals(BlockStatus.BROKEN.ordinal(), status);
    }

    /**
     * Test getStaticTrack
     */
    @Test
    public void testGetStaticTrack() throws SQLException {
        StaticTrack st = _tm.getStaticTrack();

        assertNotNull(st.getStaticBlock(1));
        assertNotNull(st.getStaticBlock(2));
        assertNotNull(st.getStaticSwitch(1));
    }

    /**
     * Test getting the next block basic linear 3 -> 4
     */
    @Test
    public void testNextBlockLinear() throws SQLException {
        StaticBlock currBlock = _tm.getStaticBlock(3);
        StaticBlock expectedNext = _tm.getStaticBlock(4);

        StaticBlock actualNext = _tm.nextBlock(currBlock, true);

        assertEquals(expectedNext, actualNext);
    }

    /**
     * Test getting the next block (reverse direction) 4 -> 3
     */
    @Test
    public void testNextBlockLinearReverse() throws SQLException {
        StaticBlock currBlock = _tm.getStaticBlock(4);
        StaticBlock expectedNext = _tm.getStaticBlock(3);

        StaticBlock actualNext = _tm.nextBlock(currBlock, false);

        assertEquals(expectedNext, actualNext);
    }

    /**
     * Test getting the next block with default position switch 1 -> 2
     */
    @Test
    public void testNextBlockSwitchDefault() throws SQLException {
        StaticBlock currBlock = _tm.getStaticBlock(1);
        StaticBlock expectedNext = _tm.getStaticBlock(2);

        StaticBlock actualNext = _tm.nextBlock(currBlock, true);

        assertEquals(expectedNext, actualNext);
    }

    /**
     * Test getting the next block with default position switch (reverse direction) 2 -> 1
     */
    @Test
    public void testNextBlockSwitchDefaultReverse() throws SQLException {
        StaticBlock currBlock = _tm.getStaticBlock(2);
        StaticBlock expectedNext = _tm.getStaticBlock(1);

        StaticBlock actualNext = _tm.nextBlock(currBlock, false);

        assertEquals(expectedNext, actualNext);
    }

    /**
     * Test getting the next block with active position switch 1 -> 3
     */
    @Test
    public void testNextBlockSwitchActive() throws SQLException {
        StaticBlock currBlock = _tm.getStaticBlock(1);
        StaticBlock expectedNext = _tm.getStaticBlock(3);

        assertTrue(_tm.setSwitch(1, true));

        StaticBlock actualNext = _tm.nextBlock(currBlock, true);
        assertEquals(expectedNext, actualNext);
    }

    /**
     * Test getting the next block 3 -> 1
     */
    @Test
    public void testNextBlockSwitchActiveReverse() throws SQLException {
        StaticBlock currBlock = _tm.getStaticBlock(3);
        StaticBlock expectedNext = _tm.getStaticBlock(1);

        assertTrue(_tm.setSwitch(1, true));

        StaticBlock actualNext = _tm.nextBlock(currBlock, false);
        assertEquals(expectedNext, actualNext);
    }

    /**
     * Test getting the next direction 3 -> 4
     */
    @Test
    public void testNextDirectionLinear() throws SQLException {
        StaticBlock curr = _tm.getStaticBlock(3);
        StaticBlock next = _tm.getStaticBlock(4);

        boolean nextDir = _tm.nextDirection(curr, next);
        assertEquals(true, nextDir);
    }

    /**
     * Test getting the next direction 4 -> 3
     */
    @Test
    public void testNextDirectionLinearReverse() throws SQLException {
        StaticBlock curr = _tm.getStaticBlock(4);
        StaticBlock next = _tm.getStaticBlock(3);

        boolean nextDir = _tm.nextDirection(curr, next);
        assertEquals(false, nextDir);
    }

    /**
     * Test getting the next direction 4 -> 5
     */
    @Test
    public void testNextDirectionLinearDirectionChange() throws SQLException {
        StaticBlock curr = _tm.getStaticBlock(4);
        StaticBlock next = _tm.getStaticBlock(5);

        boolean nextDir = _tm.nextDirection(curr, next);
        assertEquals(false, nextDir);
    }

    // 2 -> 1

    /**
     * Test getting the next direction 1 -> 2
     */
    @Test
    public void testNextDirectionSwitchDefault() throws SQLException {
        StaticBlock curr = _tm.getStaticBlock(1);
        StaticBlock next = _tm.getStaticBlock(2);

        boolean nextDir = _tm.nextDirection(curr, next);
        assertEquals(false, nextDir);
    }

    /**
     * Test getting the next direction 2 -> 1
     */
    @Test
    public void testNextDirectionSwitchDefaultReverse() throws SQLException {
        StaticBlock curr = _tm.getStaticBlock(2);
        StaticBlock next = _tm.getStaticBlock(1);

        boolean nextDir = _tm.nextDirection(curr, next);
        assertEquals(false, nextDir);
    }

    /**
     * Test getting the next direction 1 -> 3
     */
    @Test
    public void testNextDirectionSwitchActive() throws SQLException {
        StaticBlock curr = _tm.getStaticBlock(1);
        StaticBlock next = _tm.getStaticBlock(3);

        boolean nextDir = _tm.nextDirection(curr, next);
        assertEquals(true, nextDir);
    }

    /**
     * Test getting the next direction 3 -> 1
     */
    @Test
    public void testNextDirectionSwitchActiveReverse() throws SQLException {
        StaticBlock curr = _tm.getStaticBlock(3);
        StaticBlock next = _tm.getStaticBlock(1);

        boolean nextDir = _tm.nextDirection(curr, next);
        assertEquals(false, nextDir);
    }

    /**
     * Test moving a train around a simple track
     */
    @Test
    public void testUpdateTrain() throws SQLException {
        // setup environment clock time
        Environment.clock = 1;

        // add train to line on a block (train id, starting block)
        assertTrue(_tm.initializeTrain(2, 1));

        // update trackmodel
        _tm.updateTrain(1);

        // check train position
        assertEquals(2.5, _tm.getTrainPosition(1), epsilon);
        
        // check block occupancies
        ArrayList<StaticBlock> occupancy = _tm.trainOccupancy.get(1);
        assertEquals(_tm.getStaticBlock(1), occupancy.get(0));
    }

    /**
     * Validate basic train reported change get.
     */
    @Test
    public void testGetTrainReportedBlockChange() throws SQLException {
        assertEquals(false, _tm.getTrainReportedBlockChange(1));

        PreparedStatement stmt = _tm.conn.prepareStatement("UPDATE trains SET reported_change = ? WHERE id = ?;");
        stmt.setInt(1, 1);
        stmt.setInt(2, 1);
        stmt.execute();

        assertEquals(true, _tm.getTrainReportedBlockChange(1));
    }

    /**
     * Validate basic train reported passenger get.
     */
    @Test
    public void testGetTrainReportedPassengers() throws SQLException {
        assertEquals(false, _tm.getTrainReportedPassenger(1));

        PreparedStatement stmt = _tm.conn.prepareStatement("UPDATE trains SET reported_passengers = ? WHERE id = ?;");
        stmt.setInt(1, 1);
        stmt.setInt(2, 1);
        stmt.execute();

        assertEquals(true, _tm.getTrainReportedPassenger(1));
    }

        /**
     * Validate basic train reported passenger get.
     */
    @Test
    public void testGetTrainLoadedPassengers() throws SQLException {
        assertEquals(false, _tm.getTrainLoadedPassenger(1));

        PreparedStatement stmt = _tm.conn.prepareStatement("UPDATE trains SET loaded_passengers = ? WHERE id = ?;");
        stmt.setInt(1, 1);
        stmt.setInt(2, 1);
        stmt.execute();

        assertEquals(true, _tm.getTrainLoadedPassenger(1));
    }

    /**
     * Test repeatedly calling getTrainBlockChange
     */
    @Test
    public void testGetTrainBlockChange() throws SQLException {
        assertTrue(_tm.getTrainBlockChange(1));
        assertFalse(_tm.getTrainBlockChange(1));
        assertFalse(_tm.getTrainBlockChange(1));
    }

    /**
     * Test repeatedly calling getTrainBlockChange
     */
    @Test
    public void testGetTrainPassengers() throws SQLException {
        assertEquals(0, _tm.getTrainPassengers(1));

        // set reported passengers flag to false (simulate movement)
        _tm.setTrainReportedPassenger(1, false);

        assertEquals(50, _tm.getTrainPassengers(1));
        assertEquals(0, _tm.getTrainPassengers(1));
    }

    /**
     * Validate getstaticBlock(1) on green line
     */
    @Test
    public void testGetStaticBlockGreenLine() throws IOException, SQLException {
        File file = new File(
            this.getClass().getClassLoader().getResource("TrackModel/green.csv").getFile());

        // function should return true
        assertTrue(_tm.importTrack(file));

        // database should now have csv data in it
        assertNotNull(_tm.getStaticBlock(1));
    }
}