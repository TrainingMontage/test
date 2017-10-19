package trackmodel;

import java.sql.*;
import java.io.*;
import java.util.*;

public class TrackModel {

    // singleton object
    private static TrackModel model;

    // class objects
    protected Connection conn;

    /**
     * Constructs the Track Model (privately).
     *
     * constructs the database.
     */
    private TrackModel() {

        // construct the database
        String sql_create_blocks =
            "CREATE TABLE blocks (\n" +
            "   id integer PRIMARY KEY,\n" + // static properties
            "   region text NOT NULL,\n" +
            "   grade real NOT NULL,\n" +
            "   elevation real NOT NULL,\n" +
            "   length real NOT NULL,\n" +
            "   station text,\n" +
            // "   switch integer,\n" +
            // "   rr_crossing,\n" +
            // "   line text NOT NULL,\n" +
            // "   next integer NOT NULL,\n" +
            // "   bidirectional integer NOT NULL,\n" +
            // "   speed_limit integer NOT NULL,\n" +
            // "   switch_engaged integer,\n" +  // dynamic properties
            "   occupied integer\n" +
            // "   speed integer,\n" +
            // "   authority text\n" +
            ");";
        String sql_create_trains =
            "CREATE TABLE trains (\n" +
            "   id integer PRIMARY KEY,\n" +
            "   curr_block integer NOT NULL,\n" +
            "   position real NOT NULL\n" +
            ");";

        try {
            // create a connection to the database
            Class.forName("org.sqlite.JDBC");
            this.conn = DriverManager.getConnection("jdbc:sqlite::memory:");

            // create tables
            Statement stmt = conn.createStatement();
            stmt.execute(sql_create_blocks);
            stmt.execute(sql_create_trains);
        }  catch (Exception e) {
            System.err.println("Exception occured" + e);
        }
    }

    /**
     * Initialize the track model.
     * 
     * @return 
     */
    public static TrackModel init() {
        if (model == null) {
            model = new TrackModel();
        }
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
     */
    public static boolean importTrack(File file) {
        String sql_load = "INSERT INTO blocks (id,region,grade,elevation,length,station) VALUES (?, ?, ?, ?, ?, ?);";
        BufferedReader br;

        try {
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

                stmt.executeUpdate();
            }
            br.close();

            return true;
        } catch (Exception e) {
            System.err.println("Exception occurred" + e);
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Exports a model of the track to a file.
     *
     * @param      file  The file to export to
     *
     * @return     True if successful, False otherwise
     */
    public static boolean exportTrack(File file) {
        // create file if it doesn't exist
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        // write output to file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            Statement stmt = model.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id,region,grade,elevation,length,station FROM blocks");

            writer.write("id,region,grade,elevation,length,station");
            while(rs.next()) {
                String arrStr[] = {
                    rs.getString("id"),
                    rs.getString("region"),
                    rs.getString("grade"),
                    rs.getString("elevation"),
                    rs.getString("length"),
                    rs.getString("station") };
                writer.write("\n" + String.join(",", arrStr));
            }
            writer.close();
            return true;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return false;
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
     */
    public static boolean isOccupied(int blockId) {
        Integer occupied = null;
        try {
            PreparedStatement stmt = model.conn.prepareStatement("SELECT occupied FROM blocks WHERE id = ?;");
            stmt.setInt(1, blockId);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            occupied = (Integer) rs.getObject("occupied");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(occupied != null) {
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
     */
    protected static boolean setOccupied(int blockId, boolean occupied) {
        try {
            PreparedStatement stmt = model.conn.prepareStatement("UPDATE blocks SET occupied = ? WHERE id = ?;");
            if (occupied) {
                stmt.setInt(1, 1);
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }
            stmt.setInt(2, blockId);
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Gets the block ids. This is meant to be used from within the GUI
     *
     * @return     A lsit of block ids.
     */
    protected static ArrayList<String> getBlockIds() {
        ArrayList<String> blocks = new ArrayList<String>();

        try {
            Statement stmt = model.conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id FROM blocks;");

            while (rs.next()) {
                blocks.add(rs.getString("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return blocks;
    }

    /**
     * Gets the static block info for a given id.
     *
     * @param      blockId  The block identifier
     *
     * @return     The static block.
     */
    public static StaticBlock getStaticBlock(int blockId) {
        try {
            PreparedStatement stmt = model.conn.prepareStatement("SELECT id,region,grade,elevation,length,station FROM blocks WHERE id = ?");
            stmt.setInt(1, blockId);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            StaticBlock block = new StaticBlock();

            block.setId(rs.getInt("id"));
            block.setRegion(rs.getString("region"));
            block.setGrade(rs.getDouble("grade"));
            block.setElevation(rs.getDouble("elevation"));
            block.setLength(rs.getDouble("length"));
            block.setStation(rs.getString("station"));
            
            return block;
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sets the region of a block.
     *
     * @param      blockId  The block identifier
     * @param      region   The region
     *
     * @return     true if successful, false otherwise
     */
    protected static boolean setRegion(int blockId, String region) {
        try {
            PreparedStatement stmt = model.conn.prepareStatement("UPDATE blocks SET region = ? WHERE id = ?;");
            stmt.setString(1, region);
            stmt.setInt(2, blockId);
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Sets the length of a block.
     *
     * @param      blockId  The block identifier
     * @param      length   The length
     *
     * @return     true if successful, false otherwise
     */
    protected static boolean setLength(int blockId, double length) {
        try {
            PreparedStatement stmt = model.conn.prepareStatement("UPDATE blocks SET length = ? WHERE id = ?;");
            stmt.setDouble(1, length);
            stmt.setInt(2, blockId);
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Sets the elevation of a block.
     *
     * @param      blockId  The block identifier
     * @param      elevation   The elevation
     *
     * @return     true if successful, false otherwise
     */
    protected static boolean setElevation(int blockId, double elevation) {
        try {
            PreparedStatement stmt = model.conn.prepareStatement("UPDATE blocks SET elevation = ? WHERE id = ?;");
            stmt.setDouble(1, elevation);
            stmt.setInt(2, blockId);
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Sets the grade of a block.
     *
     * @param      blockId  The block identifier
     * @param      grade   The grade
     *
     * @return     true if successful, false otherwise
     */
    protected static boolean setGrade(int blockId, double grade) {
        try {
            PreparedStatement stmt = model.conn.prepareStatement("UPDATE blocks SET grade = ? WHERE id = ?;");
            stmt.setDouble(1, grade);
            stmt.setInt(2, blockId);
            stmt.execute();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}