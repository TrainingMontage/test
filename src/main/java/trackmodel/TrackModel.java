package trackmodel;

import java.sql.*;
import java.io.*;
import java.util.*;

/**
 * Class for track model. This is a singleton class, meaning that all methods
 * are accessed statically. Here is how to initialize the track model:
 *
 * <pre> {@code TrackModel.init(); } </pre>
 * 
 * To initalize with some test data loaded:
 * 
 * <pre> {@code TrackModel.initWithTestData(); } </pre>
 * 
 */
public class TrackModel {

    // singleton object
    private static TrackModel model;

    // class objects
    protected Connection conn;

    /**
     * Constructs the Track Model (privately).
     *
     * constructs the database.
     *
     * @throws SQLException could not contruct TrackModel object
     * @throws ClassNotFoundException could not find JDBC
     */
    private TrackModel() throws SQLException, ClassNotFoundException {

        // construct the database
        String sql_create_blocks =
            "CREATE TABLE blocks (\n" +
            "   id integer PRIMARY KEY,\n" + // static properties
            "   region text NOT NULL,\n" +
            "   grade real NOT NULL,\n" +
            "   elevation real NOT NULL,\n" +
            "   length real NOT NULL,\n" +
            "   station text,\n" +
            "   switch_root integer,\n" +
            "   switch_leaf integer,\n" +
            "   rr_crossing integer,\n" +
            "   line text NOT NULL,\n" +
            "   next integer NOT NULL,\n" +
            "   bidirectional integer NOT NULL,\n" +
            "   speed_limit integer NOT NULL,\n" +
            "   switch_active integer,\n" +  // dynamic properties
            "   crossing_active integer,\n" +
            "   occupied integer,\n" +
            "   speed integer,\n" +
            "   authority text\n" +
            ");";
        String sql_create_trains =
            "CREATE TABLE trains (\n" +
            "   id integer PRIMARY KEY,\n" +
            "   curr_block integer NOT NULL,\n" +
            "   position real NOT NULL\n" +
            ");";

        // create a connection to the database
        Class.forName("org.sqlite.JDBC");
        this.conn = DriverManager.getConnection("jdbc:sqlite::memory:");

        // create tables
        Statement stmt = conn.createStatement();
        stmt.execute(sql_create_blocks);
        stmt.execute(sql_create_trains);
    }

    /**
     * Initialize the track model.
     *
     * @return this TrackModel
     *
     * @throws SQLException could not initialize track
     * @throws ClassNotFoundException could not find JDBC
     */
    public static TrackModel init() throws SQLException, ClassNotFoundException {
        if (model == null) {
            model = new TrackModel();
        }
        return model;
    }

    /**
     * Initialize the track model.
     *
     * @return     the singleton instance of the track model
     */
    public static TrackModel initWithTestData() throws SQLException, ClassNotFoundException, IOException {
        // initialize model
        init();

        // import a test data track
        File testTrackFile = new File(
            TrackModel.class.getClassLoader().getResource("TrackModel/test_track.csv").getFile());
        importTrack(testTrackFile);

        return model;
    }

    /**
     * Imports a model of the track from a file.
     *
     * Reads file as csv, and inserts each row into the database.
     *
     * @param      file  The file to import
     *
     * @return     True if successful, False otherwise
     *
     * @throws      SQLException could not import track
     * @throws      IOException could not read track import
     */
    public static boolean importTrack(File file) throws SQLException, IOException {
        String sql_load = "INSERT INTO blocks " +
                          "(id,region,grade,elevation,length,station,switch_root,switch_leaf,rr_crossing,line,next,bidirectional,speed_limit) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        BufferedReader br;

        model.clearDB();

        PreparedStatement stmt = model.conn.prepareStatement(sql_load);
        br = new BufferedReader(new FileReader(file));
        String line;
        while ( (line = br.readLine()) != null) {
            String[] values = line.split(",", -1);
            int i = 0; // id
            stmt.setInt(i + 1, Integer.parseInt(values[i]));

            i++; // region
            stmt.setString(i + 1, values[i]);

            i++; // grade
            stmt.setDouble(i + 1, Double.parseDouble(values[i]));

            i++; // elevation
            stmt.setDouble(i + 1, Double.parseDouble(values[i]));

            i++; // length
            stmt.setDouble(i + 1, Double.parseDouble(values[i]));

            i++; // station
            stmt.setString(i + 1, values[i]);

            i++; // switch_root (optional)
            if (!values[i].equals("")) {
                stmt.setInt(i + 1, Integer.parseInt(values[i]));
            } else {
                stmt.setNull(i + 1, java.sql.Types.INTEGER);
            }

            i++; // switch_leaf
            if (!values[i].equals("")) {
                stmt.setInt(i + 1, Integer.parseInt(values[i]));
            } else {
                stmt.setNull(i + 1, java.sql.Types.INTEGER);
            }

            i++; // rr_crossing
            stmt.setInt(i + 1, Integer.parseInt(values[i]));

            i++; // line
            stmt.setString(i + 1, values[i]);

            i++; // next
            stmt.setInt(i + 1, Integer.parseInt(values[i]));

            i++; // bidirectional
            stmt.setInt(i + 1, Integer.parseInt(values[i]));

            i++; // speed limit
            stmt.setInt(i + 1, Integer.parseInt(values[i]));

            stmt.executeUpdate();
        }
        br.close();

        return true;
    }

    /**
     * Exports a model of the track to a file.
     *
     * @param      file  The file to export to
     *
     * @return     True if successful, False otherwise
     *
     * @throws     SQLException could not export track
     * @throws     IOException could not write to export file
     */
    public static boolean exportTrack(File file) throws SQLException, IOException {
        // create file if it doesn't exist
        if (!file.exists()) {
            file.createNewFile();
        }

        // write output to file
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        Statement stmt = model.conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id,region,grade,elevation,length,station,switch_root,switch_leaf,rr_crossing,line,next,bidirectional,speed_limit FROM blocks");

        writer.write("id,region,grade,elevation,length,station,switch_root,switch_leaf,rr_crossing,line,next,bidirectional,speed_limit");
        while (rs.next()) {
            String arrStr[] = {
                rs.getString("id"),
                rs.getString("region"),
                rs.getString("grade"),
                rs.getString("elevation"),
                rs.getString("length"),
                rs.getString("station"),
                rs.getString("switch_root"),
                rs.getString("switch_leaf"),
                rs.getString("rr_crossing"),
                rs.getString("line"),
                rs.getString("next"),
                rs.getString("bidirectional"),
                rs.getString("speed_limit")
            };

            // get rid of nulls
            for (int i = 0; i < arrStr.length; i++) {
                if (arrStr[i] == null) {
                    arrStr[i] = "";
                }
            }

            // join
            writer.write("\n" + String.join(",", arrStr));
        }
        writer.close();
        return true;
    }

    /**
     * Truncates all tables in database
     *
     * @throws     SQLException  Something went wrong when clearing
     */
    protected void clearDB() throws SQLException {
        Statement stmt = this.conn.createStatement();
        stmt.execute("DELETE FROM blocks;");
        stmt.execute("DELETE FROM trains;");
    }

    /**
     * Determines if a block occupied.
     *
     * @param      blockId  The block identifier
     *
     * @return     True if occupied, False otherwise.
     *
     * @throws     SQLException could not get occupancy
     */
    public static boolean isOccupied(int blockId) throws SQLException {
        Integer occupied = null;
        PreparedStatement stmt = model.conn.prepareStatement("SELECT occupied FROM blocks WHERE id = ?;");
        stmt.setInt(1, blockId);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        occupied = (Integer) rs.getObject("occupied");


        if (occupied != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets whether or not a block is occupied.
     *
     * @param      blockId   The block identifier
     * @param      occupied  The occupied
     *
     * @return     the new value for occupied
     *
     * @throws     SQLException could not set occupancy
     */
    protected static boolean setOccupied(int blockId, boolean occupied) throws SQLException {
        PreparedStatement stmt = model.conn.prepareStatement("UPDATE blocks SET occupied = ? WHERE id = ?;");
        if (occupied) {
            stmt.setInt(1, 1);
        } else {
            stmt.setNull(1, java.sql.Types.INTEGER);
        }
        stmt.setInt(2, blockId);
        stmt.execute();
        return true;
    }

    /**
     * Gets the block ids. This is meant to be used from within the GUI
     *
     * @return     A lsit of block ids.
     *
     * @throws     SQLException could not get block ids
     */
    protected static ArrayList<String> getBlockIds() throws SQLException {
        ArrayList<String> blocks = new ArrayList<String>();

        Statement stmt = model.conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id FROM blocks;");

        while (rs.next()) {
            blocks.add(rs.getString("id"));
        }

        return blocks;
    }

    /**
     * Gets the static block info for a given id.
     *
     * @param      blockId  The block identifier
     *
     * @return     The static block.
     *
     * @throws     SQLException could not get static block
     */
    public static StaticBlock getStaticBlock(int blockId) throws SQLException {
        return getStaticBlock(blockId, null);
    }

    /**
     * Gets the static block. This is meant for internal use.
     *
     * @param      blockId       The block identifier
     * @param      staticSwitch  The static switch
     *
     * @return     The static block.
     *
     * @throws     SQLException could not get static block
     */
    protected static StaticBlock getStaticBlock(int blockId, StaticSwitch staticSwitch) throws SQLException {
        PreparedStatement stmt = model.conn.prepareStatement("SELECT id,region,grade,elevation,length,station,switch_root,switch_leaf,next FROM blocks WHERE id = ?");
        stmt.setInt(1, blockId);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        // if there is a switch connected, this has to be handled differently to make the objects
        // point to eachother properly and we don't start an infinite loop
        StaticSwitch sw;
        if (staticSwitch == null) {
            int switch_root = rs.getInt("switch_root");
            if (!rs.wasNull()) {
                sw = getStaticSwitch(rs.getInt("switch_root"));
                return sw.getRoot();
            } else {
                int switch_leaf = rs.getInt("switch_leaf");
                if (!rs.wasNull()) {
                    sw = getStaticSwitch(switch_leaf);
                    if (sw.getInactiveLeaf().getId() == rs.getInt("id")) {
                        return sw.getInactiveLeaf();
                    } else {
                        return sw.getActiveLeaf();
                    }
                }
            }
        }

        // correct switch is already given or there is no switch
        StaticBlock block = new StaticBlock();

        block.setId(rs.getInt("id"));
        block.setRegion(rs.getString("region"));
        block.setGrade(rs.getDouble("grade"));
        block.setElevation(rs.getDouble("elevation"));
        block.setLength(rs.getDouble("length"));
        block.setStation(rs.getString("station"));
        block.setStaticSwitch(staticSwitch);
        return block;
    }


    /**
     * Gets the static switch.
     *
     * @param      switchId  The switch identifier
     *
     * @return     The static switch.
     *
     * @throws     SQLException could not get static switch
     */
    public static StaticSwitch getStaticSwitch(int switchId) throws SQLException {
        PreparedStatement stmt = model.conn.prepareStatement(
                                     "SELECT A.id as root_id, B.id as inactive_id, C.id as active_id " +
                                     "FROM blocks A " +
                                     "LEFT JOIN blocks B " +
                                     "ON (A.switch_root = B.switch_leaf and A.next = B.id) " +
                                     "LEFT JOIN blocks C " +
                                     "ON (A.switch_root = C.switch_leaf and A.next <> C.id)" +
                                     "WHERE A.switch_root = ?");
        stmt.setInt(1, switchId);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        int root_id = 1; // rs.getInt("root_id");
        int inactive_id = 2; // rs.getInt("inactive_id");
        int active_id = 3; // rs.getInt("active_id");

        StaticSwitch sw = new StaticSwitch();
        sw.setRoot(getStaticBlock(root_id, sw));
        sw.setInactiveLeaf(getStaticBlock(inactive_id, sw));
        sw.setActiveLeaf(getStaticBlock(active_id, sw));

        return sw;
    }

    /**
     * Sets the region of a block.
     *
     * @param      blockId  The block identifier
     * @param      region   The region
     *
     * @return     true if successful, false otherwise
     *
     * @throws      SQLException could not set region
     */
    protected static boolean setRegion(int blockId, String region) throws SQLException {
        PreparedStatement stmt = model.conn.prepareStatement("UPDATE blocks SET region = ? WHERE id = ?;");
        stmt.setString(1, region);
        stmt.setInt(2, blockId);
        stmt.execute();
        return true;
    }

    /**
     * Sets the length of a block.
     *
     * @param      blockId  The block identifier
     * @param      length   The length
     *
     * @return     true if successful, false otherwise
     *
     * @throws     SQLException could not set length
     */
    protected static boolean setLength(int blockId, double length) throws SQLException {
        PreparedStatement stmt = model.conn.prepareStatement("UPDATE blocks SET length = ? WHERE id = ?;");
        stmt.setDouble(1, length);
        stmt.setInt(2, blockId);
        stmt.execute();
        return true;
    }

    /**
     * Sets the elevation of a block.
     *
     * @param      blockId  The block identifier
     * @param      elevation   The elevation
     *
     * @return     true if successful, false otherwise
     *
     * @throws     SQLException could not set elevation
     */
    protected static boolean setElevation(int blockId, double elevation) throws SQLException {
        PreparedStatement stmt = model.conn.prepareStatement("UPDATE blocks SET elevation = ? WHERE id = ?;");
        stmt.setDouble(1, elevation);
        stmt.setInt(2, blockId);
        stmt.execute();
        return true;
    }

    /**
     * Sets the grade of a block.
     *
     * @param      blockId  The block identifier
     * @param      grade   The grade
     *
     * @return     true if successful, false otherwise
     *
     * @throws     SQLException could not set grade
     */
    protected static boolean setGrade(int blockId, double grade) throws SQLException {
        PreparedStatement stmt = model.conn.prepareStatement("UPDATE blocks SET grade = ? WHERE id = ?;");
        stmt.setDouble(1, grade);
        stmt.setInt(2, blockId);
        stmt.execute();
        return true;
    }

    /**
     * Sets the switch.
     *
     * @param      blockId       The block identifier
     * @param      active         The active
     *
     * @return     new value of the switch (active/inactive)
     *
     * @throws     SQLException  Something went wrong, likely the block id wasn't right.
     */
    public static boolean setSwitch(int blockId, boolean active) throws SQLException {
        PreparedStatement stmt = model.conn.prepareStatement("UPDATE blocks SET switch_active = ? WHERE id = ?;");
        stmt.setInt(1, active ? 1 : 0);
        stmt.setInt(2, blockId);
        stmt.execute();
        return active;
    }

    /**
     * Gets the switch sate.
     *
     * @param      blockId       The block identifier
     *
     * @return     The switch state. (True for engaged)
     *
     * @throws     SQLException  Something went wrong, likely the block id wasn't right.
     */
    public static boolean getSwitch(int blockId) throws SQLException {
        PreparedStatement stmt = model.conn.prepareStatement("SELECT switch_active FROM blocks WHERE id = ?");
        stmt.setInt(1, blockId);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        return rs.getInt("switch_active") > 0 ? true : false;
    }

    /**
     * Sets the authority.
     *
     * @param      blockId       The block identifier
     * @param      authority     The authority
     *
     * @return     the new authority value
     *
     * @throws     SQLException  Something went wrong, likely the block id wasn't right.
     */
    public static boolean setAuthority(int blockId, boolean authority) throws SQLException {
        PreparedStatement stmt = model.conn.prepareStatement("UPDATE blocks SET authority = ? WHERE id = ?;");
        stmt.setInt(1, authority ? 1 : 0);
        stmt.setInt(2, blockId);
        stmt.execute();
        return authority;
    }
    
    /**
     * Sets the suggested speed.
     *
     * @param      blockId       The block identifier
     * @param      speed         The speed
     *
     * @return     the new suggested speed
     *
     * @throws     SQLException  Something went wrong, likely the block id wasn't right.
     */
    public static int setSpeed(int blockId, int speed) throws SQLException {
        PreparedStatement stmt = model.conn.prepareStatement("UPDATE blocks SET speed = ? WHERE id = ?;");
        stmt.setInt(1, speed);
        stmt.setInt(2, blockId);
        stmt.execute();
        return speed;
    }

    /**
     * Gets the crossing state.
     *
     * @param      blockId       The block identifier
     *
     * @return     The crossing state.
     *
     * @throws     SQLException  Something went wrong, likely the block id wasn't right.
     */
    public static boolean getCrossingState(int blockId) throws SQLException {
        PreparedStatement stmt = model.conn.prepareStatement("SELECT crossing_active FROM blocks WHERE id = ?");
        stmt.setInt(1, blockId);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        return rs.getInt("crossing_active") > 0 ? true : false;
    }

    /**
     * Sets the crossing state.
     *
     * @param      blockId       The block identifier
     * @param      active        The active
     *
     * @return     The new crossing state
     *
     * @throws     SQLException  Something went wrong, likely the block id wasn't right.
     */
    public static boolean setCrossingState(int blockId, boolean active) throws SQLException {
        PreparedStatement stmt = model.conn.prepareStatement("UPDATE blocks SET crossing_active = ? WHERE id = ?;");
        stmt.setInt(1, active ? 1 : 0);
        stmt.setInt(2, blockId);
        stmt.execute();
        return active;
    }
}