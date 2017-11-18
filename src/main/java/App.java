import javax.swing.*;
import java.sql.SQLException;

import shared.GlobalGUI;

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
    public static void run() throws SQLException, ClassNotFoundException {
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
     * @throws SQLException in case the database didn't work
     * @throws ClassNotFoundException in case JDBC couldn't be found
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        run();
    }
}
