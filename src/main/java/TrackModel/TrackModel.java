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
            // "   grade real NOT NULL,\n" +
            // "   elevation real NOT NULL,\n" +
            "   length integer NOT NULL,\n" +
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
        String sql_load = "INSERT INTO blocks (id,region,length,station) VALUES (?, ?, ?, ?);";
        BufferedReader br;

        try {
            model.clearDB();

            PreparedStatement stmt = model.conn.prepareStatement(sql_load);
            br = new BufferedReader(new FileReader(file));
            String line;
            while ( (line = br.readLine()) != null) {
                String[] values = line.split(",", -1);    //your seperator
                stmt.setInt(1, Integer.parseInt(values[0]));
                stmt.setString(2, values[1]);
                stmt.setInt(3, Integer.parseInt(values[2]));
                stmt.setString(4, values[3]);

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
            ResultSet rs = stmt.executeQuery("SELECT id,region,length,station FROM blocks");

            writer.write("id,region,length,station");
            while(rs.next()) {
                String arrStr[] = { rs.getString("id"), rs.getString("region"), rs.getString("length"), rs.getString("station") };
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
            stmt.setInt(1, occupied?1:null);
            stmt.setInt(2, blockId);
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isOccupied(blockId);
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

}