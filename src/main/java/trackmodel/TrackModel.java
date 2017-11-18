/*   ______                 _           _                 
 *  /_  __/ _____  ____ _  (_) ____    (_) ____    ____ _ 
 *   / /   / ___/ / __ `/ / / / __ \  / / / __ \  / __ `/ 
 *  / /   / /    / /_/ / / / / / / / / / / / / / / /_/ /  
 * /_/   /_/     \__,_/ /_/ /_/ /_/ /_/ /_/ /_/  \__, /   
 *                                              /____/    
 *     __  ___                 __                        
 *    /  |/  / ____    ____   / /_  ____ _  ____ _  ___ 
 *   / /|_/ / / __ \  / __ \ / __/ / __ `/ / __ `/ / _ \
 *  / /  / / / /_/ / / / / // /_  / /_/ / / /_/ / /  __/
 * /_/  /_/  \____/ /_/ /_/ \__/  \__,_/  \__, /  \___/ 
 *                                       /____/         
 *
 * @author Alec Rosenbaum
 */

package trackmodel;

import java.sql.*;
import java.io.*;
import java.util.*;
import java.nio.ByteBuffer;
import shared.*;

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
    private static StaticTrack staticTrack = null;

    // static
    protected static int FREEZING = 32;

    // class objects
    protected Connection conn;
    protected int last_updated = 0;

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
            "   underground integer,\n" +
            "   line text NOT NULL,\n" +
            "   next integer NOT NULL,\n" +
            "   bidirectional integer NOT NULL,\n" +
            "   speed_limit integer NOT NULL,\n" +
            "   beacon integer,\n" +
            "   heater integer NOT NULL,\n" +
            "   switch_active integer,\n" +  // dynamic properties
            "   crossing_active integer,\n" +
            "   occupied integer,\n" +
            "   speed integer,\n" +
            "   authority integer,\n" +
            "   signal integer,\n" +
            "   status integer\n" +
            ");";
        String sql_create_trains =
            "CREATE TABLE trains (\n" +
            "   id integer PRIMARY KEY,\n" +
            "   curr_block integer NOT NULL,\n" +
            "   position real NOT NULL,\n" +
            "   direction integer NOT NULL\n" +
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
     * Initialize/get the track model.
     *
     * @return     this TrackModel
     *
     * @throws     SQLException            could not initialize track
     * @throws     ClassNotFoundException  could not find JDBC
     */
    public static TrackModel getTrackModel() throws SQLException, ClassNotFoundException {
        if (model == null) {
            model = new TrackModel();
        }
        return model;
    }

    /**
     * Initializes the Track Mdel
     *
     * @return     An instance of the track model
     *
     * @throws     SQLException            could not initialize track
     * @throws     ClassNotFoundException  could not find JDBC
     */
    public static TrackModel init() throws SQLException, ClassNotFoundException {
        return TrackModel.getTrackModel();
    }

    /**
     * Initialize the track model.
     *
     * @return     the singleton instance of the track model
     *
     * @throws     SQLException            Couldn't initialize the model
     * @throws     ClassNotFoundException  Couldn't initialize the model
     * @throws     IOException             Couldn't import the track
     */
    public static TrackModel initWithTestData() throws SQLException, ClassNotFoundException, IOException {
        // initialize model
        TrackModel tm = getTrackModel();

        // import a test data track
        File testTrackFile = new File(
            TrackModel.class.getClassLoader().getResource("TrackModel/test_track.csv").getFile());
        tm.importTrack(testTrackFile);

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
    public boolean importTrack(File file) throws SQLException, IOException {
        String sql_load = "INSERT INTO blocks " +
                          "(id,region,grade,elevation,length,station,switch_root,switch_leaf,rr_crossing,underground,line,next,bidirectional,speed_limit,beacon,heater) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        BufferedReader br;

        this.clearDB();

        PreparedStatement stmt = this.conn.prepareStatement(sql_load);
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

            i++; // underground
            stmt.setInt(i + 1, Integer.parseInt(values[i]));

            i++; // line
            stmt.setString(i + 1, values[i]);

            i++; // next
            stmt.setInt(i + 1, Integer.parseInt(values[i]));

            i++; // bidirectional
            stmt.setInt(i + 1, Integer.parseInt(values[i]));

            i++; // speed limit
            stmt.setInt(i + 1, Integer.parseInt(values[i]));

            i++; // beacon
            if (!values[i].equals("")) {
                stmt.setInt(i + 1, Integer.parseInt(values[i]));
            } else {
                stmt.setNull(i + 1, java.sql.Types.INTEGER);
            }

            i++; // heater
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
    public boolean exportTrack(File file) throws SQLException, IOException {
        // create file if it doesn't exist
        if (!file.exists()) {
            file.createNewFile();
        }

        // write output to file
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        Statement stmt = this.conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id,region,grade,elevation,length,station,switch_root,switch_leaf,rr_crossing,underground,line,next,bidirectional,speed_limit,beacon,heater FROM blocks");

        boolean first = true;
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
                rs.getString("underground"),
                rs.getString("line"),
                rs.getString("next"),
                rs.getString("bidirectional"),
                rs.getString("speed_limit"),
                rs.getString("beacon"),
                rs.getString("heater")
            };

            // get rid of nulls
            for (int i = 0; i < arrStr.length; i++) {
                if (arrStr[i] == null) {
                    arrStr[i] = "";
                }
            }

            // join
            if (!first) {
                writer.write("\n");
            } else {
                first = false;
            }
            writer.write(String.join(",", arrStr));
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
    public boolean isOccupied(int blockId) throws SQLException {
        Integer occupied = null;
        PreparedStatement stmt = this.conn.prepareStatement("SELECT occupied FROM blocks WHERE id = ?;");
        stmt.setInt(1, blockId);
        ResultSet rs = stmt.executeQuery();

        rs.next();
        occupied = (Integer) rs.getObject("occupied");

        return occupied != null;
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
    protected boolean setOccupied(int blockId, boolean occupied) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET occupied = ? WHERE id = ?;");
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
     * Gets the block ids
     *
     * @return     A list of block ids.
     *
     * @throws     SQLException could not get block ids
     */
    public ArrayList<Integer> getBlockIds() throws SQLException {
        ArrayList<Integer> blocks = new ArrayList<Integer>();

        Statement stmt = this.conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id FROM blocks;");

        while (rs.next()) {
            blocks.add(rs.getInt("id"));
        }

        return blocks;
    }

    /**
     * Gets the switch ids
     *
     * @return     A list of switch ids.
     *
     * @throws     SQLException could not get switch ids
     */
    public ArrayList<Integer> getSwitchIds() throws SQLException {
        ArrayList<Integer> switches = new ArrayList<Integer>();

        Statement stmt = this.conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT DISTINCT switch_root as id FROM blocks where switch_root is not NULL;");

        while (rs.next()) {
            switches.add(rs.getInt("id"));
        }

        return switches;
    }

    /**
     * Gets the train ids
     *
     * @return     A list of train ids.
     *
     * @throws     SQLException could not get train ids
     */
    public ArrayList<Integer> getTrainIds() throws SQLException {
        ArrayList<Integer> trains = new ArrayList<Integer>();

        Statement stmt = this.conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT id FROM trains;");

        while (rs.next()) {
            trains.add(rs.getInt("id"));
        }

        return trains;
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
    public StaticBlock getStaticBlock(int blockId) throws SQLException {
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
    protected StaticBlock getStaticBlock(int blockId, StaticSwitch staticSwitch) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("SELECT id,region,grade,elevation,length,station,switch_root,switch_leaf,next FROM blocks WHERE id = ?");
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
                    if (sw.getDefaultLeaf().getId() == rs.getInt("id")) {
                        return sw.getDefaultLeaf();
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
        block.setNextId(rs.getInt("next"));
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
    public StaticSwitch getStaticSwitch(int switchId) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement(
                                     "SELECT A.switch_root as switch_id, A.id as root_id, B.id as inactive_id, C.id as active_id " +
                                     "FROM blocks A " +
                                     "LEFT JOIN blocks B " +
                                     "ON (A.switch_root = B.switch_leaf and A.next = B.id) " +
                                     "LEFT JOIN blocks C " +
                                     "ON (A.switch_root = C.switch_leaf and A.next <> C.id)" +
                                     "WHERE A.switch_root = ?");
        stmt.setInt(1, switchId);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        int root_id = rs.getInt("root_id");
        int inactive_id = rs.getInt("inactive_id");
        int active_id = rs.getInt("active_id");

        StaticSwitch sw = new StaticSwitch(rs.getInt("switch_id"));
        sw.setRoot(getStaticBlock(root_id, sw));
        sw.setDefaultLeaf(getStaticBlock(inactive_id, sw));
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
    protected boolean setRegion(int blockId, String region) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET region = ? WHERE id = ?;");
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
    protected boolean setLength(int blockId, double length) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET length = ? WHERE id = ?;");
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
    protected boolean setElevation(int blockId, double elevation) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET elevation = ? WHERE id = ?;");
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
    protected boolean setGrade(int blockId, double grade) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET grade = ? WHERE id = ?;");
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
    public boolean setSwitch(int blockId, boolean active) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET switch_active = ? WHERE id = ?;");
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
    public boolean getSwitch(int blockId) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("SELECT switch_active FROM blocks WHERE id = ?");
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
    public boolean setAuthority(int blockId, boolean authority) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET authority = ? WHERE id = ?;");
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
    public int setSpeed(int blockId, int speed) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET speed = ? WHERE id = ?;");
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
    public boolean getCrossingState(int blockId) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("SELECT crossing_active FROM blocks WHERE id = ?");
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
    public boolean setCrossingState(int blockId, boolean active) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET crossing_active = ? WHERE id = ?;");
        stmt.setInt(1, active ? 1 : 0);
        stmt.setInt(2, blockId);
        stmt.execute();
        return active;
    }

    /**
     * Sets the signal on a block.
     *
     * @param      blockId       The block identifier
     * @param      active        The value
     *
     * @return     the new signal state
     *
     * @throws     SQLException  Something went wrong, likely the block id
     *                           wasn't right.
     */
    public boolean setSignal(int blockId, boolean active) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET signal = ? WHERE id = ?;");
        stmt.setInt(1, active ? 1 : 0);
        stmt.setInt(2, blockId);
        stmt.execute();
        return active;
    }

    /**
     * Gets the signal of a block.
     *
     * @param      blockId       The block identifier
     *
     * @return     The signal.
     *
     * @throws     SQLException  Something went wrong, likely the block id
     *                           wasn't right.
     */
    public boolean getSignal(int blockId) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("SELECT signal FROM blocks WHERE id = ?");
        stmt.setInt(1, blockId);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        return ((Integer) rs.getObject("signal")) != null;
    }

    /**
     * Initialize a new train
     *
     * @param      trainId           The train identifier
     * @param      starting_blockId  The starting block id
     *
     * @return     true if successful, false otherwise
     *
     * @throws     SQLException      Something went wrong, likely the block id
     *                               wasn't valid
     */
    public boolean initializeTrain(int trainId, int starting_blockId) throws SQLException {
        String sql_load = "INSERT INTO trains " +
                          "(id,curr_block,position,direction) " +
                          "VALUES (?, ?, ?, 0);";

        PreparedStatement stmt = this.conn.prepareStatement(sql_load);

        int i = 1;
        stmt.setInt(i++, trainId);  // train id
        stmt.setInt(i++, starting_blockId); // curr_block
        stmt.setDouble(i++, 0);
        stmt.executeUpdate();

        return true;
    }

    /**
     * Gets the train's commanded authority.
     *
     * @param      trainId       The train identifier
     *
     * @return     The train authority.
     *
     * @throws     SQLException  Something went wrong, likely the block id
     *                           wasn't valid
     */
    public int getTrainAuthority(int trainId) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("SELECT authority FROM blocks bl left join trains tr on tr.curr_block = bl.id WHERE tr.id = ?");
        stmt.setInt(1, trainId);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        Integer authority = (Integer) rs.getObject("authority");
        return authority == null ? 0 : authority;
    }

    /**
     * Gets the train's suggested speed.
     *
     * @param      trainId       The train identifier
     *
     * @return     The train speed.
     *
     * @throws     SQLException  Something went wrong, likely the block id
     *                           wasn't valid
     */
    public double getTrainSpeed(int trainId) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("SELECT speed FROM blocks bl left join trains tr on tr.curr_block = bl.id WHERE tr.id = ?");
        stmt.setInt(1, trainId);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        Integer speed = (Integer) rs.getObject("speed");
        return speed == null ? 0 : speed;
    }

    /**
     * Returns beacon information if train in within range of a beacon.
     *
     * @param      trainId       The train identifier
     *
     * @return     The train beacon.
     *
     * @throws     SQLException  Something went wrong, likely the block id
     *                           wasn't valid
     */
    public byte[] getTrainBeacon(int trainId) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("SELECT beacon FROM blocks bl left join trains tr on tr.curr_block = bl.id WHERE tr.id = ?");
        stmt.setInt(1, trainId);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        Integer beacon =  (Integer) rs.getObject("beacon");
        if (beacon != null) {
            return ByteBuffer.allocate(4).putInt(beacon).array();
        } else {
            return null;
        }
    }

    /**
     * Determines if track is icy.
     *
     * @param      trainId       The train identifier
     *
     * @return     True if icy track, False otherwise.
     *
     * @throws     SQLException  Something went wrong, likely the block id
     *                           wasn't valid
     */
    public boolean isIcyTrack(int trainId) throws SQLException {
        if (Environment.temperature > FREEZING) {
            return false;
        }

        PreparedStatement stmt = this.conn.prepareStatement("SELECT heater FROM blocks bl left join trains tr on tr.curr_block = bl.id WHERE tr.id = ?");
        stmt.setInt(1, trainId);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        return rs.getInt("heater") == 0;
    }

    /**
     * Gets the grade of the block a train is on.
     *
     * @param      trainId       The train identifier
     *
     * @return     The grade.
     *
     * @throws     SQLException  Something went wrong, likely the block id
     *                           wasn't valid
     */
    public double getGrade(int trainId) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("SELECT grade FROM blocks bl left join trains tr on tr.curr_block = bl.id WHERE tr.id = ?");
        stmt.setInt(1, trainId);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        return rs.getDouble("grade");
    }

    /**
     * Gets the status of a block.
     *
     * @param      blockId       The block identifier
     *
     * @return     The status.
     *
     * @throws     SQLException  Something went wrong, likely the block id
     *                           wasn't valid
     */
    public BlockStatus getStatus(int blockId) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("SELECT status FROM blocks WHERE id = ?");
        stmt.setInt(1, blockId);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        Integer status = (Integer) rs.getObject("status");
        return BlockStatus.values()[status == null ? 0 : status];
    }

    /**
     * Sets the status of a block.
     *
     * @param      blockId       The block identifier
     * @param      status        The status
     *
     * @return     { description_of_the_return_value }
     *
     * @throws     SQLException  Something went wrong, likely the block id
     *                           wasn't valid
     */
    public BlockStatus setStatus(int blockId, BlockStatus status) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET status = ? WHERE id = ?;");
        stmt.setInt(1, status.ordinal());
        stmt.setInt(2, blockId);
        stmt.execute();
        return status;
    }

    /**
     * Gets the static track.
     *
     * @return     The static track.
     *
     * @throws     SQLException  Something went wrong, I don't know what would cause this
     */
    public StaticTrack getStaticTrack() throws SQLException {
        if (this.staticTrack == null) {
            this.staticTrack = new StaticTrack();
            for (Integer blockId : this.getBlockIds()) {
                this.staticTrack.putStaticBlock(this.getStaticBlock(blockId));
            }
            for (Integer switchId : this.getSwitchIds()) {
                this.staticTrack.putStaticSwitch(this.getStaticSwitch(switchId));
            }
        }

        return this.staticTrack;
    }


    protected void update() throws SQLException {
        if (this.last_updated == Environment.clock) {
            return;
        }

        for (int trainId : this.getTrainIds()) {
            updateTrain(trainId);
        }
        this.last_updated = Environment.clock;
    }

    protected void updateTrain(int trainId) throws SQLException {
        // TODO
        // fake actually calling a train until there's something there to call
        // ...getDisplacement()
        double displacement = 2.50; // hardcode 2.5m displacement for now

        StaticBlock curr_block = this.getStaticBlock(this.getTrainBlock(trainId));
        double position = this.getTrainPosition(trainId);
        boolean direction = this.getTrainDirection(trainId);

        if (position + displacement > curr_block.getLength()) {
            // move to the next block
            StaticBlock next_block = this.nextBlock(curr_block, direction);
            boolean newDirection = this.nextDirection(curr_block, next_block);

            // actually update table
            this.setTrainBlock(trainId, next_block.getId());
            this.setTrainDirection(trainId, newDirection);
            this.setTrainPosition(trainId, position + displacement - curr_block.getLength());
        } else {
            // update position on same block
            this.setTrainPosition(trainId, position + displacement);
        }
    }

    /**
     * Gets the train's current position (internal use only)
     *
     * @param      trainId       The train identifier
     *
     * @return     The train's position within the block
     *
     * @throws     SQLException  Something went wrong, likely the block id
     *                           wasn't valid
     */
    protected double getTrainPosition(int trainId) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("SELECT position FROM trains WHERE id = ?");
        stmt.setInt(1, trainId);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        double position = rs.getDouble("position");
        return position;
    }

    /**
     * Gets the train's current position (internal use only)
     *
     * @param      trainId       The train identifier
     *
     * @return     The train's position within the block
     *
     * @throws     SQLException  Something went wrong, likely the block id
     *                           wasn't valid
     */
    protected double setTrainPosition(int trainId, double position) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("UPDATE trains SET position = ? WHERE id = ?;");
        stmt.setDouble(1, position);
        stmt.setInt(2, trainId);
        stmt.execute();
        return position;
    }

    /**
     * Gets the train's current direction (internal use only)
     *
     * @param      trainId       The train identifier
     *
     * @return     The train's direction within the block
     *
     * @throws     SQLException  Something went wrong, likely the block id
     *                           wasn't valid
     */
    protected boolean getTrainDirection(int trainId) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("SELECT direction FROM trains WHERE id = ?");
        stmt.setInt(1, trainId);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        int direction = rs.getInt("direction");
        return direction  > 0 ? true : false;
    }

    /**
     * Gets the train's current direction (internal use only)
     *
     * @param      trainId       The train identifier
     *
     * @return     The train's direction within the block
     *
     * @throws     SQLException  Something went wrong, likely the block id
     *                           wasn't valid
     */
    protected boolean setTrainDirection(int trainId, boolean direction) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("UPDATE trains SET direction = ? WHERE id = ?;");
        stmt.setInt(1, direction ? 1 : 0);
        stmt.setInt(2, trainId);
        stmt.execute();
        return direction;
    }

    /**
     * Gets the train's current block (internal use only)
     *
     * @param      trainId       The train identifier
     *
     * @return     The static block that train is on
     *
     * @throws     SQLException  Something went wrong, likely the block id
     *                           wasn't valid
     */
    protected int getTrainBlock(int trainId) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("SELECT curr_block FROM trains WHERE id = ?");
        stmt.setInt(1, trainId);
        ResultSet rs = stmt.executeQuery();
        rs.next();

        int curr_block = rs.getInt("curr_block");
        return curr_block;
    }

    /**
     * Sets the train's current block (internal use only)
     *
     * @param      trainId       The train identifier
     * @param      block         The new block
     *
     * @return     The static block that train is on
     *
     * @throws     SQLException  Something went wrong, likely the block id
     *                           wasn't valid
     */
    protected int setTrainBlock(int trainId, int block) throws SQLException {
        PreparedStatement stmt = this.conn.prepareStatement("UPDATE trains SET curr_block = ? WHERE id = ?;");
        stmt.setDouble(1, block);
        stmt.setInt(2, trainId);
        stmt.execute();
        return block;
    }

    protected StaticBlock nextBlock(StaticBlock curr_block, boolean direction) throws SQLException {
        StaticSwitch sw = curr_block.getStaticSwitch();
        // System.err.println("Looking up block: " + curr_block.getNextId());
        StaticBlock next = this.getStaticBlock(curr_block.getNextId());
        if (sw != null && ((sw.contains(next) && direction) || (!sw.contains(next) && !direction))) {
            // moving towards a switch

            if (curr_block.equals(sw.getRoot())) { // current block is the root
                return this.getSwitch(sw.getId()) ? sw.getActiveLeaf() : sw.getDefaultLeaf();
            }
            if (curr_block.equals(sw.getActiveLeaf())) { // current block is the active leaf
                if (this.getSwitch(sw.getId())) {
                    return sw.getRoot();
                }
                throw new CrashIntoSwitchException();
            }
            if (curr_block.equals(sw.getDefaultLeaf())) { // current block is the default leaf
                if (!this.getSwitch(sw.getId())) {
                    return sw.getRoot();
                }
                throw new CrashIntoSwitchException();
            }
        } else { // not moving towards a switch
            if (direction) {
                return this.getStaticBlock(curr_block.getNextId());
            } else {
                // lookup and return previous block
                PreparedStatement stmt = this.conn.prepareStatement("SELECT id FROM blocks WHERE next = ?");
                stmt.setInt(1, curr_block.getId());
                ResultSet rs = stmt.executeQuery();
                rs.next();
                return this.getStaticBlock(rs.getInt("id"));
            }
        }
        // error?
        return null;
    }

    protected boolean nextDirection(StaticBlock curr_block, StaticBlock next_block) {
        if (next_block.getNextId() == curr_block.getId() || (next_block.getStaticSwitch() != null && next_block.getStaticSwitch().getActiveLeaf().equals(curr_block))) {
            return false;
        } else {
            return true;
        }
    }

    // public int getPassengers(int trainId); TODO
}