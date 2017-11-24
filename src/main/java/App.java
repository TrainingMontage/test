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
 * @author All
 */

import javax.swing.*;
import java.sql.SQLException;
import java.io.File;
import java.util.Arrays;

import shared.GlobalGUI;
import shared.Environment;
import trackmodel.TrackModel;
import wayside.WaysideController;
import CTCModel.CTCModel;
import trainmodel.TrainTracker;
import traincontroller.TrainController;

/**
 * This Java source file was generated by the Gradle 'init' task.
 *
 * A simple class to demonstrate some JavaDoc, among other tools.
 * These tools could include:
 * <ol>
 * <li> Google's style guide. I think there's a tool to automatically enforce this.
 * <li> JUnit, the unit-testing framework for Java.
 * <li> Eventually, a Java build tool of some kind (Gradle, Maven, whatever).
 * <li> Travis CI, which is going to build everything, run our tests, 
 *      tell us if they fail, build the docs, and push them to a GitHub Pages site.
 * </ol>
 */
public class App {
    private static int speedMultiplier = 1;
    
    /**
     * Runs our app, mane!
     * @throws SQLException in case DB stuff.
     * @throws ClassNotFoundException in case JDBC in not found.
     */
    public static void run() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GlobalGUI.createAndShowGUI();
            }
        });
    }

    /**
     * Here we have the all-important entry point into our application.
     *
     * @param args Command-line arguments.
     * @throws SQLException in case DB stuff.
     * @throws ClassNotFoundException in case JDBC in not found.
     */
    public static void main(String[] args) {
        //init each module
        TrackModel.init();
        if (args.length > 0) {
            // System.out.println("importing " + args[0]);
            TrackModel.getTrackModel().importTrack(new File(args[0]));
            // System.out.println(Arrays.toString(TrackModel.getTrackModel().getBlockIds().toArray()));
        } 
        WaysideController.init();
        CTCModel.init();
        TrainTracker.getTrainTracker();
        TrainController.initUI();
        
        //open the main gui
        run();
        
        //the main update loop
        while(true){
            CTCModel.update();
            Environment.clock += speedMultiplier;
        }
    }
}
