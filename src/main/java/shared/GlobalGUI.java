package shared;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

import trackmodel.RunTrackModel;
import CTCModel.CTCGUI;
import wayside.WaysideController;
import trainmodel.TrainModelGUI;

public class GlobalGUI {

    public static void addComponentsToPane(Container pane) {
        JButton button;
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;

        button = new JButton("CTC");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                CTCGUI.run();
            }
        } );
        c.gridx = 0;
        c.gridy = 0;
        pane.add(button, c);

        button = new JButton("Wayside");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                WaysideController.openWindow();
            }
        } );
        c.gridx = 0;
        c.gridy = 1;
        pane.add(button, c);

        button = new JButton("TrackModel");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    RunTrackModel.run();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } );
        c.gridx = 0;
        c.gridy = 2;
        pane.add(button, c);

        button = new JButton("TrainModel");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                TrainModelGUI gui = new TrainModelGUI();
                gui.setVisible(true);
            }
        } );
        c.gridx = 0;
        c.gridy = 3;
        pane.add(button, c);

        button = new JButton("TrainController");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO
            }
        } );
        c.gridx = 0;
        c.gridy = 4;
        pane.add(button, c);

    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("GridBagLayoutDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}
