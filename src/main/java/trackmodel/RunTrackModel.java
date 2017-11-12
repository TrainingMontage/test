package trackmodel;

import javax.swing.*;
import java.sql.SQLException;

public class RunTrackModel {
    public static void run() throws SQLException, ClassNotFoundException {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI.createAndShowGUI();
            }
        });
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        TrackModel.init();
        run();
    }
}