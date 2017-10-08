package TrackModel;

import javax.swing.*;

public class RunTrackModel {
    public static void run() {
        TrackModel.init();

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI.createAndShowGUI();
            }
        });    
    }
}