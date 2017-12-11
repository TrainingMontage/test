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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.nio.ByteBuffer;
import java.util.Random;

import shared.Environment;
import shared.TrainCrashException;
import shared.CrashIntoSwitchException;
import shared.BlockStatus;
import trainmodel.TrainTracker;
import trainmodel.Train;

/**
 * Class for track model. This is a singleton class, meaning that all methods
 * are accessed statically. Here is how to initialize the track model:
 *
 * <pre> {@code TrackModel.init(); } </pre>
 *
 * To initalize with some test data loaded:
 *
 * <pre> {@code TrackModel.initWithTestData(); } </pre>
 */
public class TrackModel implements TrackModelInterface {

    // singleton object
    private static TrackModel model;
    private StaticTrack staticTrack = null;

    // static
    protected static final int FREEZING = 32;
    protected static final double ACCUMULATION_RATE = .1;

    // class objects
    protected Connection conn;
    protected int last_updated = 0;
    protected HashMap<Integer, ArrayList<StaticBlock>> trainOccupancy;
    protected HashMap<Integer, StaticBlock> staticBlockCache;
    protected HashMap<Integer, StaticSwitch> staticSwitchCache;
    protected HashMap<Integer, Boolean> blockOccupancy;
    protected HashMap<Integer, Boolean> blockAuthority;
    protected HashMap<Integer, Double> blockSpeed;
    protected HashMap<Integer, Boolean> switchState;
    protected HashMap<Integer, Boolean> crossingState;
    protected HashMap<Integer, Double> waitingPassengers;
    protected Random random;

    /**
     * Constructs the Track Model (privately).
     *
     * constructs the database.
     */
    protected TrackModel() {
        try {
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
                "   speed real,\n" +
                "   authority integer,\n" +
                "   signal integer,\n" +
                "   status integer\n" +
                ");";
            String sql_create_trains =
                "CREATE TABLE trains (\n" +
                "   id integer PRIMARY KEY,\n" +
                "   curr_block integer NOT NULL,\n" +
                "   position real NOT NULL,\n" +
                "   direction integer NOT NULL,\n" +
                "   reported_change integer NOT NULL,\n" +
                "   reported_passengers integer NOT NULL,\n" +
                "   loaded_passengers integer NOT NULL\n" +
                ");";

            // create a connection to the database
            Class.forName("org.sqlite.JDBC");
            this.conn = DriverManager.getConnection("jdbc:sqlite::memory:");


            // create tables
            Statement stmt = conn.createStatement();
            stmt.execute(sql_create_blocks);
            stmt.execute(sql_create_trains);

            // initalize train occupancy
            this.trainOccupancy = new HashMap<Integer, ArrayList<StaticBlock>>();
            this.staticBlockCache = new HashMap<Integer, StaticBlock>();
            this.staticSwitchCache = new HashMap<Integer, StaticSwitch>();
            this.blockOccupancy = new HashMap<Integer, Boolean>();
            this.blockAuthority = new HashMap<Integer, Boolean>();
            this.blockSpeed = new HashMap<Integer, Double>();
            this.switchState = new HashMap<Integer, Boolean>();
            this.crossingState = new HashMap<Integer, Boolean>();
            this.waitingPassengers = new HashMap<Integer, Double>();

            // initalize random
            this.random = new Random();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize/get the track model.
     *
     * @return     this TrackModel
     */
    public static TrackModel getTrackModel() {
        if (model == null) {
            model = new TrackModel();
        }
        return model;
    }

    /**
     * Initializes the Track Mdel
     *
     * @return     An instance of the track model
     */
    public static TrackModel init() {
        return TrackModel.getTrackModel();
    }

    /**
     * Initialize the track model.
     *
     * @return     the singleton instance of the track model
     */
    public static TrackModel initWithTestData() {
        // initialize model
        TrackModel tm = getTrackModel();

        // import a test data track
        File testTrackFile = new File(
            TrackModel.class.getClassLoader().getResource("TrackModel/test_track.csv").getFile());
        tm.importTrack(testTrackFile);

        return tm;
    }

    /**
     * load test data.
     *
     * @return     the singleton instance of the track model
     */
    public TrackModel loadTestData() {
        // import a test data track
        File testTrackFile = new File(
            TrackModel.class.getClassLoader().getResource("TrackModel/test_track.csv").getFile());
        this.importTrack(testTrackFile);

        return this;
    }

    /**
     * This is useful for forcing random during testing
     *
     * @param      r     the new random object to set
     */
    protected void setRandom(Random r) {
        this.random = r;
    }

    /**
     * Imports a model of the track from a file.
     *
     * Reads file as csv, and inserts each row into the database.
     *
     * @param      file  The file to import
     *
     * @return     True if successful, False otherwise
     */
    public boolean importTrack(File file) {
        String sql_load = "INSERT INTO blocks " +
                          "(id,region,grade,elevation,length,station,switch_root,switch_leaf,rr_crossing,underground,line,next,bidirectional,speed_limit,beacon,heater) " +
                          "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        BufferedReader br;

        this.clearDB();

        try (PreparedStatement stmt = this.conn.prepareStatement(sql_load)) {
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
                stmt.setDouble(i + 1, Double.parseDouble(values[i]));

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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    /**
     * Exports a model of the track to a file.
     *
     * @param      file  The file to export to
     *
     * @return     True if successful, False otherwise
     */
    public boolean exportTrack(File file) {
        try (Statement stmt = this.conn.createStatement()) {
            // create file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile();
            }

            // write output to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Truncates all tables in database
     */
    protected void clearDB() {
        try (Statement stmt = this.conn.createStatement()) {
            stmt.execute("DELETE FROM blocks;");
            stmt.execute("DELETE FROM trains;");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Determines if a block occupied.
     *
     * @param      blockId  The block identifier
     *
     * @return     True if occupied (or broken), False otherwise.
     */
    public boolean isOccupied(int blockId) {
        this.update();

        switch (this.getStatus(blockId)) {
        case BROKEN:
        case IN_REPAIR:
        case FORCE_OCCUPIED:
        case TRACK_CIRCUIT_FAILURE:
        case POWER_FAILURE:
            return true;
        case FORCE_UNOCCUPIED:
            return false;
        case OPERATIONAL:
        default:
            break;
        }

        Boolean occupied = blockOccupancy.get(blockId);
        return occupied == null ? false : occupied;
    }

    /**
     * Sets whether or not a block is occupied.
     *
     * @param      blockId   The block identifier
     * @param      occupied  The occupied
     *
     * @return     the new value for occupied
     */
    protected boolean setOccupied(int blockId, boolean occupied) {
        blockOccupancy.put(blockId, occupied);
        return blockOccupancy.get(blockId);
    }

    /**
     * Gets the block ids
     *
     * @return     A list of block ids.
     */
    public ArrayList<Integer> getBlockIds() {
        ArrayList<Integer> blocks = new ArrayList<Integer>();

        try (Statement stmt = this.conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT id FROM blocks;");

            while (rs.next()) {
                blocks.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return blocks;
    }

    /**
     * Gets the switch ids
     *
     * @return     A list of switch ids.
     */
    public ArrayList<Integer> getSwitchIds() {
        ArrayList<Integer> switches = new ArrayList<Integer>();

        try (Statement stmt = this.conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT DISTINCT switch_root as id FROM blocks where switch_root is not NULL;");

            while (rs.next()) {
                switches.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return switches;
    }

    /**
     * Gets the train ids
     *
     * @return     A list of train ids.
     */
    public ArrayList<Integer> getTrainIds() {
        ArrayList<Integer> trains = new ArrayList<Integer>();

        try (Statement stmt = this.conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id FROM trains;");

            while (rs.next()) {
                trains.add(rs.getInt("id"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return trains;
    }

    /**
     * Gets the static block info for a given id.
     *
     * @param      blockId  The block identifier
     *
     * @return     The static block.
     */
    public StaticBlock getStaticBlock(int blockId) {
        StaticBlock blk = staticBlockCache.get(blockId);
        if (blk == null || blk.needsUpdate()) {
            // update block
            staticBlockCache.put(blockId, getStaticBlock(blockId, null));
        }
        return staticBlockCache.get(blockId);
    }

    /**
     * Gets the static block. This is meant for internal use.
     *
     * @param      blockId       The block identifier
     * @param      staticSwitch  The static switch
     *
     * @return     The static block.
     */
    protected StaticBlock getStaticBlock(int blockId, StaticSwitch staticSwitch) {
        try (PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM blocks WHERE id = ?")) {
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
            block.setSpeedLimit(rs.getDouble("speed_limit"));
            block.setStation(rs.getString("station"));
            block.setLine(rs.getString("line"));
            block.setNextId(rs.getInt("next"));
            block.setBeacon(rs.getInt("beacon"));
            block.setHeater(rs.getInt("heater") > 0 ? true : false);
            block.setCrossing(rs.getInt("rr_crossing") > 0 ? true : false);
            block.setUnderground(rs.getInt("underground") > 0 ? true : false);
            block.setBidirectional(rs.getInt("bidirectional") > 0 ? true : false);
            block.setStaticSwitch(staticSwitch);

            // determine and set previous id
            try (PreparedStatement sub_stmt = this.conn.prepareStatement("SELECT id FROM blocks WHERE next = ? AND next <> id")) {
                sub_stmt.setInt(1, blockId);
                rs = sub_stmt.executeQuery();
                if (rs.next()) {
                    block.setPreviousId(rs.getInt("id"));
                }
            }

            return block;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Gets the static switch.
     *
     * @param      switchId  The switch identifier
     *
     * @return     The static switch.
     */
    public StaticSwitch getStaticSwitch(int switchId) {
        StaticSwitch sw = staticSwitchCache.get(switchId);
        if (sw != null) {
            return sw;
        }

        String statement = "SELECT A.switch_root as switch_id, A.id as root_id, B.id as inactive_id, C.id as active_id " +
                           "FROM blocks A " +
                           "LEFT JOIN blocks B " +
                           "ON (A.switch_root = B.switch_leaf) " +
                           "LEFT JOIN blocks C " +
                           "ON (A.switch_root = C.switch_leaf)" +
                           "WHERE A.switch_root = ? AND ABS(A.id - B.id) = 1 AND B.id <> C.id";

        try (PreparedStatement stmt = this.conn.prepareStatement(statement)) {
            stmt.setInt(1, switchId);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            int root_id = rs.getInt("root_id");
            int inactive_id = rs.getInt("inactive_id");
            int active_id = rs.getInt("active_id");

            sw = new StaticSwitch(rs.getInt("switch_id"));
            sw.setRoot(getStaticBlock(root_id, sw));
            sw.setDefaultLeaf(getStaticBlock(inactive_id, sw));
            sw.setActiveLeaf(getStaticBlock(active_id, sw));
            staticSwitchCache.put(switchId, sw);
            return sw;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the region of a block.
     *
     * @param      blockId  The block identifier
     * @param      region   The region
     *
     * @return     true if successful, false otherwise
     */
    protected boolean setRegion(int blockId, String region) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET region = ? WHERE id = ?;")) {
            stmt.setString(1, region);
            stmt.setInt(2, blockId);
            stmt.execute();
            setStaticBlockUpdate(blockId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the length of a block.
     *
     * @param      blockId  The block identifier
     * @param      length   The length
     *
     * @return     true if successful, false otherwise
     */
    protected boolean setLength(int blockId, double length) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET length = ? WHERE id = ?;")) {
            stmt.setDouble(1, length);
            stmt.setInt(2, blockId);
            stmt.execute();
            setStaticBlockUpdate(blockId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the speed_limit of a block.
     *
     * @param      blockId  The block identifier
     * @param      speed_limit   The speed_limit
     *
     * @return     true if successful, false otherwise
     */
    protected boolean setSpeedLimit(int blockId, double speed_limit) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET speed_limit = ? WHERE id = ?;")) {
            stmt.setDouble(1, speed_limit);
            stmt.setInt(2, blockId);
            stmt.execute();
            setStaticBlockUpdate(blockId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the elevation of a block.
     *
     * @param      blockId    The block identifier
     * @param      elevation  The elevation
     *
     * @return     true if successful, false otherwise
     */
    protected boolean setElevation(int blockId, double elevation) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET elevation = ? WHERE id = ?;")) {
            stmt.setDouble(1, elevation);
            stmt.setInt(2, blockId);
            stmt.execute();
            setStaticBlockUpdate(blockId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the grade of a block.
     *
     * @param      blockId  The block identifier
     * @param      grade    The grade
     *
     * @return     true if successful, false otherwise
     */
    protected boolean setGrade(int blockId, double grade) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET grade = ? WHERE id = ?;")) {
            stmt.setDouble(1, grade);
            stmt.setInt(2, blockId);
            stmt.execute();
            setStaticBlockUpdate(blockId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the switch.
     *
     * @param      blockId  The block identifier
     * @param      active   The active
     *
     * @return     new value of the switch (active/inactive)
     */
    public boolean setSwitch(int blockId, boolean active) {
        StaticBlock blk = this.getStaticBlock(blockId);
        int rootId = blk.getStaticSwitch().getRoot().getId();

        this.switchState.put(blockId, active);
        return this.switchState.get(blockId);
    }

    /**
     * Gets the switch state.
     *
     * @param      blockId  The block identifier
     *
     * @return     The switch state. (True for engaged)
     */
    public boolean getSwitch(int blockId) {
        StaticBlock blk = this.getStaticBlock(blockId);
        int rootId = blk.getStaticSwitch().getRoot().getId();

        Boolean state = this.switchState.get(rootId);
        return state == null ? false : state;
    }

    /**
     * Sets the authority.
     *
     * @param      blockId    The block identifier
     * @param      authority  The authority
     *
     * @return     the new authority value
     */
    public boolean setAuthority(int blockId, boolean authority) {
        blockAuthority.put(blockId, authority);
        return blockAuthority.get(blockId);
    }

    /**
     * Gets the authority of a block.
     *
     * @param      blockId  The block identifier
     *
     * @return     The authority
     */
    public boolean getAuthority(int blockId) {
        Boolean authority = blockAuthority.get(blockId);
        return authority == null ? false : authority;
    }

    /**
     * Sets the suggested speed.
     *
     * @param      blockId  The block identifier
     * @param      speed    The speed
     *
     * @return     the new suggested speed
     */
    public double setSpeed(int blockId, double speed) {
        blockSpeed.put(blockId, speed);
        return blockSpeed.get(blockId);
    }

    /**
     * Gets the crossing state.
     *
     * @param      blockId  The block identifier
     *
     * @return     The crossing state.
     */
    public boolean getCrossingState(int blockId) {
        Boolean state = this.crossingState.get(blockId);
        return state == null ? false : state;
    }

    /**
     * Sets the crossing state.
     *
     * @param      blockId  The block identifier
     * @param      active   The active
     *
     * @return     The new crossing state
     */
    public boolean setCrossingState(int blockId, boolean active) {
        this.crossingState.put(blockId, active);
        return this.crossingState.get(blockId);
    }

    /**
     * Sets the signal on a block.
     *
     * @param      blockId  The block identifier
     * @param      active   The value
     *
     * @return     the new signal state
     *
     * wasn't right.
     */
    public boolean setSignal(int blockId, boolean active) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET signal = ? WHERE id = ?;")) {
            stmt.setInt(1, active ? 1 : 0);
            stmt.setInt(2, blockId);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return active;
    }

    /**
     * Gets the signal of a block.
     *
     * @param      blockId  The block identifier
     *
     * @return     The signal.
     *
     * wasn't right.
     */
    public boolean getSignal(int blockId) {
        try (PreparedStatement stmt = this.conn.prepareStatement("SELECT signal FROM blocks WHERE id = ?")) {
            stmt.setInt(1, blockId);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            return ((Integer) rs.getObject("signal")) != null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize a new train
     *
     * @param      trainId           The train identifier
     * @param      starting_blockId  The starting block id
     *
     * @return     true if successful, false otherwise
     *
     * wasn't valid
     */
    public boolean initializeTrain(int trainId, int starting_blockId) {
        String sql_load = "INSERT INTO trains " +
                          "(id,curr_block,position,direction,reported_change,reported_passengers,loaded_passengers) " +
                          "VALUES (?, ?, ?, 1, 0, 0, 0);";

        try (PreparedStatement stmt = this.conn.prepareStatement(sql_load)) {

            int i = 1;
            stmt.setInt(i++, trainId);  // train id
            stmt.setInt(i++, starting_blockId); // curr_block
            stmt.setDouble(i++, 0);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * Gets the train's commanded authority.
     *
     * @param      trainId  The train identifier
     *
     * @return     The train authority.
     *
     * wasn't valid
     */
    public boolean getTrainAuthority(int trainId) {
        this.update();

        Integer blkId = this.getTrainBlock(trainId);

        switch (this.getStatus(blkId)) {
        case COMM_FAILURE:
            return false;
        case OPERATIONAL:
        default:
            break;
        }

        return this.getAuthority(blkId);
    }

    /**
     * Gets the train's suggested speed.
     *
     * @param      trainId  The train identifier
     *
     * @return     The train speed.
     *
     * wasn't valid
     */
    public double getTrainSpeed(int trainId) {
        this.update();

        int blkId = this.getTrainBlock(trainId);
        switch (this.getStatus(blkId)) {
        case COMM_FAILURE:
            return -1;
        case OPERATIONAL:
        default:
            break;
        }

        Double speed = this.blockSpeed.get(blkId);
        return speed == null ? 0 : speed;
    }

    /**
     * Returns beacon information if train in within range of a beacon.
     *
     * @param      trainId  The train identifier
     *
     * @return     The train beacon.
     *
     * wasn't valid
     */
    public int getTrainBeacon(int trainId) {
        this.update();

        // beacon is directly read by the train, not communicated by the track. COMM_FAILURE doesn't affect this.

        try (PreparedStatement stmt = this.conn.prepareStatement("SELECT beacon FROM blocks bl left join trains tr on tr.curr_block = bl.id WHERE tr.id = ?")) {
            stmt.setInt(1, trainId);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            Integer beacon =  (Integer) rs.getObject("beacon");
            if (beacon != null) {
                return beacon;
            } else {
                return -1;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Determines if track is icy.
     *
     * @param      trainId  The train identifier
     *
     * @return     True if icy track, False otherwise.
     *
     * wasn't valid
     */
    public boolean isIcyTrack(int trainId) {
        this.update();

        if (Environment.temperature > FREEZING) {
            return false;
        }

        try (PreparedStatement stmt = this.conn.prepareStatement("SELECT heater FROM blocks bl left join trains tr on tr.curr_block = bl.id WHERE tr.id = ?")) {
            stmt.setInt(1, trainId);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            return rs.getInt("heater") == 0;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the grade of the block a train is on.
     *
     * @param      trainId  The train identifier
     *
     * @return     The grade.
     *
     * wasn't valid
     */
    public double getGrade(int trainId) {
        this.update();

        try (PreparedStatement stmt = this.conn.prepareStatement("SELECT grade FROM blocks bl left join trains tr on tr.curr_block = bl.id WHERE tr.id = ?")) {
            stmt.setInt(1, trainId);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            return rs.getDouble("grade");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the status of a block.
     *
     * @param      blockId  The block identifier
     *
     * @return     The status.
     *
     */
    protected BlockStatus getStatus(int blockId) {
        try (PreparedStatement stmt = this.conn.prepareStatement("SELECT status FROM blocks WHERE id = ?")) {
            stmt.setInt(1, blockId);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            Integer status = (Integer) rs.getObject("status");
            return BlockStatus.values()[status == null ? 0 : status];

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the status of a block.
     *
     * @param      blockId  The block identifier
     * @param      status   The status
     *
     * @return     { description_of_the_return_value }
     *
     * wasn't valid
     */
    protected BlockStatus setStatus(int blockId, BlockStatus status) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET status = ? WHERE id = ?;")) {
            stmt.setInt(1, status.ordinal());
            stmt.setInt(2, blockId);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return status;
    }

    /**
     * Generates and returns a static track.
     *
     * @return     The static track.
     */
    public StaticTrack getStaticTrack() {
        this.staticTrack = new StaticTrack();
        for (Integer blockId : this.getBlockIds()) {
            this.staticTrack.putStaticBlock(this.getStaticBlock(blockId));
        }
        for (Integer switchId : this.getSwitchIds()) {
            this.staticTrack.putStaticSwitch(this.getStaticSwitch(switchId));
        }

        return this.staticTrack;
    }

    /**
     * update the state of the track model. moves trains and updates occupancies.
     */
    protected void update() {
        if (this.last_updated == Environment.clock) {
            return;
        }
        int diff = Environment.clock - this.last_updated;
        this.last_updated = Environment.clock;

        // accumulate passengers
        this.accumulateWaitingPassengers(this.ACCUMULATION_RATE, diff);

        // update trains
        for (int trainId : this.getTrainIds()) {
            this.updateTrain(trainId);
        }

        // rebuild occupancies
        this.updateOccupancies();
    }

    /**
     * updates the location of a train on the track
     *
     * @param      trainId  The train identifier
     */
    protected void updateTrain(int trainId) {
        Train train = this.getTrainModelFromTrainTracker(trainId);
        double displacement = train.getDisplacement();
        double train_length = train.getLength();

        StaticBlock curr_block = this.getStaticBlock(this.getTrainBlock(trainId));
        double position = this.getTrainPosition(trainId);
        boolean direction = this.getTrainDirection(trainId);

        if (position + displacement > curr_block.getLength()) {
            // move to the next block
            StaticBlock next_block = this.nextBlock(curr_block, direction);
            boolean newDirection = this.nextDirection(curr_block, next_block);

            // actually update table
            this.setTrainBlock(trainId, next_block.getId());
            this.setTrainReportedBlockChange(trainId, false);
            this.setTrainReportedPassenger(trainId, false);
            this.setTrainDirection(trainId, newDirection);
            this.setTrainPosition(trainId, position + displacement - curr_block.getLength());
        } else {
            // update position on same block
            this.setTrainPosition(trainId, position + displacement);
        }

        // update occupancy list
        curr_block = this.getStaticBlock(this.getTrainBlock(trainId));
        ArrayList<StaticBlock> occupancyList = this.trainOccupancy.get(trainId);
        if (occupancyList == null) {
            occupancyList = new ArrayList<StaticBlock>();
        }
        if (!occupancyList.contains(curr_block)) {
            occupancyList.add(curr_block);
        }

        // occupancy list can only be as long as the train is
        int sum = 0;
        for (StaticBlock block : occupancyList) {
            sum += block.getLength();
        }
        double overhang = sum - occupancyList.get(occupancyList.size() - 1).getLength() - train_length + this.getTrainPosition(trainId);
        while (overhang > occupancyList.get(0).getLength())  {
            occupancyList.remove(0);

            sum = 0;
            for (StaticBlock block : occupancyList) {
                sum += block.getLength();
            }
            overhang = sum - occupancyList.get(occupancyList.size() - 1).getLength() - train_length + this.getTrainPosition(trainId);
        }
        this.trainOccupancy.put(trainId, occupancyList);
    }

    /**
     * Gets the train's current position (internal use only)
     *
     * @param      trainId  The train identifier
     *
     * @return     The train's position within the block
     */
    protected double getTrainPosition(int trainId) {
        try (PreparedStatement stmt = this.conn.prepareStatement("SELECT position FROM trains WHERE id = ?")) {
            stmt.setInt(1, trainId);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            double position = rs.getDouble("position");
            return position;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the train's current position (internal use only)
     *
     * @param      trainId   The train identifier
     * @param      position  The position
     *
     * @return     The train's position within the block
     *
     * wasn't valid
     */
    private double setTrainPosition(int trainId, double position) {
        System.err.println("Setting Train " + trainId + " to position " + position);
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE trains SET position = ? WHERE id = ?;")) {
            stmt.setDouble(1, position);
            stmt.setInt(2, trainId);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return position;
    }

    /**
     * Gets the train's current direction (internal use only)
     *
     * @param      trainId  The train identifier
     *
     * @return     The train's direction within the block
     *
     * wasn't valid
     */
    private boolean getTrainDirection(int trainId) {
        try (PreparedStatement stmt = this.conn.prepareStatement("SELECT direction FROM trains WHERE id = ?")) {
            stmt.setInt(1, trainId);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            int direction = rs.getInt("direction");
            return direction  > 0 ? true : false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the train's current direction (internal use only)
     *
     * @param      trainId    The train identifier
     * @param      direction  The direction
     *
     * @return     The train's direction within the block
     */
    protected boolean setTrainDirection(int trainId, boolean direction) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE trains SET direction = ? WHERE id = ?;")) {
            stmt.setInt(1, direction ? 1 : 0);
            stmt.setInt(2, trainId);
            stmt.execute();
            return direction;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the train's current block (internal use only)
     *
     * @param      trainId  The train identifier
     *
     * @return     The static block that train is on
     */
    protected int getTrainBlock(int trainId) {
        try (PreparedStatement stmt = this.conn.prepareStatement("SELECT curr_block FROM trains WHERE id = ?")) {
            stmt.setInt(1, trainId);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            int curr_block = rs.getInt("curr_block");
            return curr_block;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the train's current block (internal use only)
     *
     * @param      trainId  The train identifier
     * @param      block    The new block
     *
     * @return     The static block that train is on
     *
     * wasn't valid
     */
    protected int setTrainBlock(int trainId, int block) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE trains SET curr_block = ? WHERE id = ?;")) {
            stmt.setDouble(1, block);
            stmt.setInt(2, trainId);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return block;
    }

    /**
     * { function_description }
     *
     * @param      curr_block  The curr block
     * @param      direction   The direction
     *
     * @return     { description_of_the_return_value }
     */
    protected StaticBlock nextBlock(StaticBlock curr_block, boolean direction) {
        System.err.println("Getting next block. Curr block: " + curr_block + " " + direction);
        StaticSwitch sw = curr_block.getStaticSwitch();
        StaticBlock next = this.getStaticBlock(curr_block.getNextId());
        if (sw != null && ((sw.contains(next) && direction) || (!sw.contains(next) && !direction))) {
            // moving towards a switch

            if (curr_block.equals(sw.getRoot())) { // current block is the root
                return this.getSwitch(curr_block.getId()) ? sw.getActiveLeaf() : sw.getDefaultLeaf();
            }
            if (curr_block.equals(sw.getActiveLeaf())) { // current block is the active leaf
                if (this.getSwitch(curr_block.getId())) {
                    return sw.getRoot();
                }
                throw new CrashIntoSwitchException();
            }
            if (curr_block.equals(sw.getDefaultLeaf())) { // current block is the default leaf
                if (!this.getSwitch(curr_block.getId())) {
                    return sw.getRoot();
                }
                throw new CrashIntoSwitchException();
            }
        } else { // not moving towards a switch
            if (direction) {
                return this.getStaticBlock(curr_block.getNextId());
            } else {
                // lookup and return previous block
                try (PreparedStatement stmt = this.conn.prepareStatement("SELECT id FROM blocks WHERE next = ?")) {
                    stmt.setInt(1, curr_block.getId());
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    return this.getStaticBlock(rs.getInt("id"));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        // error?
        return null;
    }

    /**
     * determines a new direction based on current and next block
     *
     * @param      curr_block  The curr block
     * @param      next_block  The next block
     *
     * @return     the direction relative to the next block
     */
    protected boolean nextDirection(StaticBlock curr_block, StaticBlock next_block) {
        System.err.print("Getting next direction: " + curr_block + " -> " + next_block + " : ");
        StaticSwitch sw = curr_block.getStaticSwitch();
        if (sw != null && sw.contains(next_block)) {
            // going over a switch
            if (sw.getRoot().equals(curr_block)) { // root node
                if (next_block.getNextId() == curr_block.getId()) {
                    System.err.println("false");
                    return false;
                } else {
                    System.err.println("true");
                    return true;
                }
            } else {
                if (sw.getRoot().getNextId() == sw.getActiveLeaf().getId() || sw.getRoot().getNextId() == sw.getDefaultLeaf().getId()) {
                    System.err.println("false");
                    return false;
                } else {
                    System.err.println("true");
                    return true;
                }
            }
        } else {
            // linear
            if (curr_block.getNextId() == next_block.getId() && next_block.getNextId() != curr_block.getId()) {
                System.err.println("true");
                return true;
            } else {
                System.err.println("false");
                return false;
            }
        }
    }

    /**
     * update the occupancy status of each block on the track
     */
    protected void updateOccupancies() {
        try {
            // destroy previous occupancy
            this.blockOccupancy = new HashMap<Integer, Boolean>();

            for (Map.Entry<Integer, ArrayList<StaticBlock>> entry : this.trainOccupancy.entrySet()) {
                Integer key = entry.getKey();
                ArrayList<StaticBlock> occupancies = entry.getValue();

                for (StaticBlock block : occupancies) {
                    if (this.isOccupied(block.getId())) {
                        throw new TrainCrashException();
                    }
                    System.err.println("TrackModel updating occupancy: " + block.getId() + " " + key);
                    this.setOccupied(block.getId(), true);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the train's reported block change flag
     *
     * @param      trainId  The train identifier
     *
     * @return     true if change has been reported, false otherwise
     */
    protected boolean getTrainReportedBlockChange(int trainId) {
        try (PreparedStatement stmt = this.conn.prepareStatement("SELECT reported_change FROM trains WHERE id = ?")) {
            stmt.setInt(1, trainId);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            return rs.getInt("reported_change") > 0 ? true : false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the train's reported block change flag (internal use only)
     *
     * @param      trainId          The train identifier
     * @param      reported_change  whether the change has been reported
     *
     * @return     new reported_change value
     */
    protected boolean setTrainReportedBlockChange(int trainId, boolean reported_change) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE trains SET reported_change = ? WHERE id = ?;")) {
            stmt.setDouble(1, reported_change ? 1 : 0);
            stmt.setInt(2, trainId);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return reported_change;
    }

    /**
     * Gets the train block change.
     *
     * @param      trainId  The train identifier
     *
     * @return     The train block change.
     */
    public boolean getTrainBlockChange(int trainId) {
        this.update();

        // This is detected directly by the train, it's not affected by a comms failure

        if (!this.getTrainReportedBlockChange(trainId)) {
            this.setTrainReportedBlockChange(trainId, true);
            return true;
        }
        return false;
    }

    /**
     * Gets the train's reported passenger flag
     *
     * @param      trainId  The train identifier
     *
     * @return     true if change has been reported, false otherwise
     */
    protected boolean getTrainReportedPassenger(int trainId) {
        try (PreparedStatement stmt = this.conn.prepareStatement("SELECT reported_passengers FROM trains WHERE id = ?")) {
            stmt.setInt(1, trainId);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            return rs.getInt("reported_passengers") > 0 ? true : false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the train's reported passenger flag (internal use only)
     *
     * @param      trainId              The train identifier
     * @param      reported_passengers  whether the change has been reported
     *
     * @return     new reported_passengers value
     */
    protected boolean setTrainReportedPassenger(int trainId, boolean reported_passengers) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE trains SET reported_passengers = ? WHERE id = ?;")) {
            stmt.setDouble(1, reported_passengers ? 1 : 0);
            stmt.setInt(2, trainId);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return reported_passengers;
    }

    /**
     * Gets the train's loaded passenger flag
     *
     * @param      trainId  The train identifier
     *
     * @return     true if change has been loaded, false otherwise
     */
    protected boolean getTrainLoadedPassenger(int trainId) {
        try (PreparedStatement stmt = this.conn.prepareStatement("SELECT loaded_passengers FROM trains WHERE id = ?")) {
            stmt.setInt(1, trainId);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            return rs.getInt("loaded_passengers") > 0 ? true : false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets the train's loaded passenger flag (internal use only)
     *
     * @param      trainId            The train identifier
     * @param      loaded_passengers  whether the change has been loaded
     *
     * @return     new loaded_passengers value
     */
    protected boolean setTrainLoadedPassenger(int trainId, boolean loaded_passengers) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE trains SET loaded_passengers = ? WHERE id = ?;")) {
            stmt.setDouble(1, loaded_passengers ? 1 : 0);
            stmt.setInt(2, trainId);
            stmt.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return loaded_passengers;
    }

    /**
     * Gets the number of passengers disembarked on a train. Returns
     * 0 if already called at a station, or if not at a station.
     *
     * @param      trainId  The train identifier
     *
     * @return     The train passengers.
     */
    public int getTrainPassengers(int trainId) {
        this.update();

        Train train = this.getTrainModelFromTrainTracker(trainId);

        // if passenger count hasn't been reported and it's a station
        if (!this.getTrainReportedPassenger(trainId) && this.getStaticBlock(this.getTrainBlock(trainId)).getStation() != null) {
            this.setTrainReportedPassenger(trainId, true);

            // disembark current passengers
            int disembarked = 0;
            int trainPassengers = train.getPassengers();
            if (trainPassengers > 0) {
                disembarked = this.random.nextInt((int) trainPassengers);
                train.setPassengers(trainPassengers - disembarked);
            }

            // load waiting passengers
            Double passengers = this.waitingPassengers.get(this.getTrainBlock(trainId));
            if (passengers != null) {
                int loaded = this.random.nextInt(Math.min((int) passengers.doubleValue(), train.getMaxPassengers()));
                passengers -= loaded;

                this.waitingPassengers.put(this.getTrainBlock(trainId), passengers - loaded);
                train.setPassengers(train.getPassengers() + loaded);
            }

            return disembarked;
        }

        return 0;
    }

    /**
     * Sets whether the block is underground.
     *
     * @param      blockId       The block identifier
     * @param      underground   whether block is underground
     *
     * @return     new value of the underground flag
     */
    protected boolean setUnderground(int blockId, boolean underground) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET underground = ? WHERE id = ?;")) {

            stmt.setInt(1, underground ? 1 : 0);
            stmt.setInt(2, blockId);
            stmt.execute();
            setStaticBlockUpdate(blockId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return underground;
    }

    /**
     * Sets whether the block has a heater.
     *
     * @param      blockId       The block identifier
     * @param      heater        whether block has a heater
     *
     * @return     new value of the heater flag
     */
    protected boolean setHeater(int blockId, boolean heater) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET heater = ? WHERE id = ?;")) {
            stmt.setInt(1, heater ? 1 : 0);
            stmt.setInt(2, blockId);
            stmt.execute();
            setStaticBlockUpdate(blockId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return heater;
    }

    /**
     * Sets whether the block has a rr_crossing.
     *
     * @param      blockId       The block identifier
     * @param      rr_crossing        whether block has a rr_crossing
     *
     * @return     new value of the rr_crossing flag
     */
    protected boolean setCrossing(int blockId, boolean rr_crossing) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET rr_crossing = ? WHERE id = ?;")) {
            stmt.setInt(1, rr_crossing ? 1 : 0);
            stmt.setInt(2, blockId);
            stmt.execute();
            setStaticBlockUpdate(blockId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return rr_crossing;
    }

    /**
     * Sets whether the block is bidirectional.
     *
     * @param      blockId         The block identifier
     * @param      bidirectional   whether block is bidirectional
     *
     * @return     new value of the bidirectional flag
     */
    protected boolean setBidirectional(int blockId, boolean bidirectional) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET bidirectional = ? WHERE id = ?;")) {
            stmt.setInt(1, bidirectional ? 1 : 0);
            stmt.setInt(2, blockId);
            stmt.execute();
            setStaticBlockUpdate(blockId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return bidirectional;
    }

    /**
     * Sets the block's station.
     *
     * @param      blockId         The block identifier
     * @param      station   name of the station
     *
     * @return     new station name
     */
    protected String setStation(int blockId, String station) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET station = ? WHERE id = ?;")) {
            stmt.setString(1, station);
            stmt.setInt(2, blockId);
            stmt.execute();
            setStaticBlockUpdate(blockId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return station;
    }

    /**
     * Sets the block's line.
     *
     * @param      blockId         The block identifier
     * @param      line   name of the line
     *
     * @return     new line name
     */
    protected String setLine(int blockId, String line) {
        try (PreparedStatement stmt = this.conn.prepareStatement("UPDATE blocks SET line = ? WHERE id = ?;")) {
            stmt.setString(1, line);
            stmt.setInt(2, blockId);
            stmt.execute();
            setStaticBlockUpdate(blockId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return line;
    }

    protected Train getTrainModelFromTrainTracker(int trainId) {
        return TrainTracker.getTrainTracker().getTrain(trainId);
    }

    public boolean setRepair(int blockId) {
        try {
            this.setStatus(blockId, BlockStatus.IN_REPAIR);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean setOperational(int blockId) {
        try {
            this.setStatus(blockId, BlockStatus.OPERATIONAL);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setStaticBlockUpdate(int blockId) {
        StaticBlock blk = staticBlockCache.get(blockId);
        if (blk != null) {
            blk.setNeedsUpdate(true);
            staticBlockCache.put(blockId, blk);
        }
    }

    /**
     * Accumulate passengers at all stations over time.
     *
     * @param      accumulationRate  The accumulation rate in # people/time unit
     * @param      timeUnits         The time units since last update
     */
    protected void accumulateWaitingPassengers(double accumulationRate, int timeUnits) {
        for (int id : this.getBlockIds()) {
            String station = this.getStaticBlock(id).getStation();
            if (station != null && station.length() > 0) {
                Double p = this.waitingPassengers.get(id);
                if (p == null) {
                    p = 0.0;
                }

                p += accumulationRate * timeUnits;
                this.waitingPassengers.put(id, p);
            }
        }
    }
}