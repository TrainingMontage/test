package TrackModel;

import java.sql.*;
import java.io.*;

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
     * Initialized the track model.
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
     * @param      filename  The filename
     *
     * @return     True if successful, False otherwise
     */
    public static boolean importTrack(File file) {
        String sql_load = "INSERT INTO blocks (id,region,length,station,occupied) VALUES (?, ?, ?, ?, ?);";
        BufferedReader br;

        try {
            PreparedStatement stmt = model.conn.prepareStatement(sql_load);
            br = new BufferedReader(new FileReader(file));
            String line;
            while ( (line = br.readLine()) != null) {
                String[] values = line.split(",", -1);    //your seperator
                System.out.println(values[4]);
                stmt.setInt(1, Integer.parseInt(values[0]));
                stmt.setString(2, values[1]);
                stmt.setInt(3, Integer.parseInt(values[2]));
                stmt.setString(4, values[3]);
                if (values[4].length() > 0) {
                    stmt.setInt(5, Integer.parseInt(values[4]));
                } else {
                    stmt.setNull(5, java.sql.Types.INTEGER);
                }

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
     * @param      filename  The filename
     *
     * @return     True if successful, False otherwise
     */
    public static boolean exportTrack(String filename) {
        // TODO
        return false;
    }

}