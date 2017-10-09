package trackmodel;

import java.awt.*;
import javax.swing.*;

public class GUI {

    protected static JButton reloadBlockInfo, submitChanges;
    protected static JCheckBox occupied;
    protected static JComboBox blockID;


    public static void addComponentsToPane(Container pane) {
        JButton button;
        JLabel label;
        JTextArea textArea;
        JCheckBox checkbox;
        JComboBox comboBox;
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JPanel panelGlobals = new JPanel();
        JPanel panelBlockInfo = new JPanel();
        JPanel panelRight = new JPanel();
        JPanel panelSwitchInfo = new JPanel();
        JPanel panelForceMajeure = new JPanel();
        JPanel panelImportExport = new JPanel();

        // ******************** globals panel *******************
        panelGlobals.setLayout(new GridBagLayout());

        label = new JLabel("11:24 AM");
        c.insets = new Insets(10, 10, 10, 10); //top padding
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        panelGlobals.add(label, c);

        label = new JLabel("76 F");
        c.gridx = 1;
        c.gridy = 0;
        panelGlobals.add(label, c);

        panelGlobals.setBorder(BorderFactory.createTitledBorder("Global Status"));
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        pane.add(panelGlobals, c);
        c.gridwidth = 1;

        // ************** Block Information panel ***************
        panelBlockInfo.setLayout(new GridBagLayout());

        // left half

        label = new JLabel("Wayside ID:");
        c.insets = new Insets(2, 2, 2, 2); //no padding
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.gridx = 0;
        c.gridy = 0;
        panelBlockInfo.add(label, c);

        textArea = new JTextArea("1");
        c.gridx = 1;
        c.gridy = 0;
        panelBlockInfo.add(textArea, c);

        label = new JLabel("Speed Limit:");
        c.gridx = 0;
        c.gridy = 1;
        panelBlockInfo.add(label, c);

        textArea = new JTextArea("15 mph");
        c.gridx = 1;
        c.gridy = 1;
        panelBlockInfo.add(textArea, c);

        label = new JLabel("Length:");
        c.gridx = 0;
        c.gridy = 2;
        panelBlockInfo.add(label, c);

        textArea = new JTextArea(".2 mi");
        c.gridx = 1;
        c.gridy = 2;
        panelBlockInfo.add(textArea, c);

        label = new JLabel("Grade:");
        c.gridx = 0;
        c.gridy = 3;
        panelBlockInfo.add(label, c);

        textArea = new JTextArea(".5 %");
        c.gridx = 1;
        c.gridy = 3;
        panelBlockInfo.add(textArea, c);

        label = new JLabel("Underground:");
        c.gridx = 0;
        c.gridy = 4;
        panelBlockInfo.add(label, c);

        checkbox = new JCheckBox();
        c.gridx = 1;
        c.gridy = 4;
        panelBlockInfo.add(checkbox, c);

        label = new JLabel("Bidirectional:");
        c.gridx = 0;
        c.gridy = 5;
        panelBlockInfo.add(label, c);

        checkbox = new JCheckBox();
        c.gridx = 1;
        c.gridy = 5;
        panelBlockInfo.add(checkbox, c);

        label = new JLabel("Occupied:");
        c.gridx = 0;
        c.gridy = 6;
        panelBlockInfo.add(label, c);

        occupied = new JCheckBox();
        c.gridx = 1;
        c.gridy = 6;
        panelBlockInfo.add(occupied, c);

        label = new JLabel("Track Heater:");
        c.gridx = 0;
        c.gridy = 7;
        panelBlockInfo.add(label, c);

        checkbox = new JCheckBox();
        c.gridx = 1;
        c.gridy = 7;
        panelBlockInfo.add(checkbox, c);

        label = new JLabel("Infrastructure:");
        c.gridx = 0;
        c.gridy = 8;
        panelBlockInfo.add(label, c);

        textArea = new JTextArea("SWITCH");
        c.gridwidth = 3;
        c.gridx = 1;
        c.gridy = 8;
        panelBlockInfo.add(textArea, c);
        c.gridwidth = 1;

        button = new JButton("Reload Block Info");
        c.gridx = 0;
        c.gridy = 9;
        c.gridwidth = 2;
        panelBlockInfo.add(button, c);
        c.gridwidth = 1;

        // right half

        label = new JLabel("Block ID:");
        c.gridx = 2;
        c.gridy = 0;
        panelBlockInfo.add(label, c);

        String[] comboOptions =  { "A1", "A2", "A3", ".." };
        blockID = new JComboBox(comboOptions);
        c.gridx = 3;
        c.gridy = 0;
        panelBlockInfo.add(blockID, c);

        label = new JLabel("Next Blocks:");
        c.gridx = 2;
        c.gridy = 1;
        panelBlockInfo.add(label, c);

        textArea = new JTextArea("A2, A3");
        c.gridx = 3;
        c.gridy = 1;
        panelBlockInfo.add(textArea, c);

        label = new JLabel("Elevation:");
        c.gridx = 2;
        c.gridy = 2;
        panelBlockInfo.add(label, c);

        textArea = new JTextArea("232 ft");
        c.gridx = 3;
        c.gridy = 2;
        panelBlockInfo.add(textArea, c);

        label = new JLabel("State:");
        c.gridx = 2;
        c.gridy = 3;
        panelBlockInfo.add(label, c);

        textArea = new JTextArea("Operational");
        c.gridx = 3;
        c.gridy = 3;
        panelBlockInfo.add(textArea, c);

        label = new JLabel("Line:");
        c.gridx = 2;
        c.gridy = 4;
        panelBlockInfo.add(label, c);

        textArea = new JTextArea("Green");
        c.gridx = 3;
        c.gridy = 4;
        panelBlockInfo.add(textArea, c);

        label = new JLabel("RR Crossing:");
        c.gridx = 2;
        c.gridy = 5;
        panelBlockInfo.add(label, c);

        checkbox = new JCheckBox();
        c.gridx = 3;
        c.gridy = 5;
        panelBlockInfo.add(checkbox, c);

        label = new JLabel("Authority:");
        c.gridx = 2;
        c.gridy = 6;
        panelBlockInfo.add(label, c);

        textArea = new JTextArea("Super Green");
        c.gridx = 3;
        c.gridy = 6;
        panelBlockInfo.add(textArea, c);

        label = new JLabel("Signal:");
        c.gridx = 2;
        c.gridy = 7;
        panelBlockInfo.add(label, c);

        textArea = new JTextArea("Super Green");
        c.gridx = 3;
        c.gridy = 7;
        panelBlockInfo.add(textArea, c);

        button = new JButton("Submit Changes");
        c.gridx = 2;
        c.gridy = 9;
        c.gridwidth = 2;
        panelBlockInfo.add(button, c);
        c.gridwidth = 1;

        panelBlockInfo.setBorder(BorderFactory.createTitledBorder("Block Info"));
        c.gridx = 0;
        c.gridy = 1;
        pane.add(panelBlockInfo, c);

        // ************** Switch Information panel ***************
        panelSwitchInfo.setLayout(new GridBagLayout());

        label = new JLabel("Switch ID:");
        c.insets = new Insets(2, 2, 2, 2); //no padding
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        panelSwitchInfo.add(label, c);

        label = new JLabel("2");
        c.gridx = 1;
        c.gridy = 0;
        panelSwitchInfo.add(label, c);

        label = new JLabel("Connected Blocks:");
        c.gridx = 0;
        c.gridy = 1;
        panelSwitchInfo.add(label, c);

        label = new JLabel("A1, A2, A3");
        c.gridx = 1;
        c.gridy = 1;
        panelSwitchInfo.add(label, c);

        label = new JLabel("Switch State:");
        c.gridx = 0;
        c.gridy = 2;
        panelSwitchInfo.add(label, c);

        label = new JLabel("A1 -> A3");
        c.gridx = 1;
        c.gridy = 2;
        panelSwitchInfo.add(label, c);

        panelSwitchInfo.setBorder(BorderFactory.createTitledBorder("Switch Info"));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panelRight.setLayout(new GridBagLayout()); // must init layout before adding things
        panelRight.add(panelSwitchInfo, c);

        // ***************** Force Majeure panel *****************
        panelForceMajeure.setLayout(new GridBagLayout());

        button = new JButton("Power Failure");
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        panelForceMajeure.add(button, c);

        button = new JButton("Broken Rail");
        c.gridx = 1;
        c.gridy = 0;
        panelForceMajeure.add(button, c);

        button = new JButton("Extra Train");
        c.gridx = 0;
        c.gridy = 1;
        panelForceMajeure.add(button, c);

        button = new JButton("Track Ciruit Failure");
        c.gridx = 1;
        c.gridy = 1;
        panelForceMajeure.add(button, c);

        button = new JButton("No Train");
        c.gridx = 0;
        c.gridy = 2;
        panelForceMajeure.add(button, c);

        button = new JButton("No Communications");
        c.gridx = 1;
        c.gridy = 2;
        panelForceMajeure.add(button, c);

        panelForceMajeure.setBorder(BorderFactory.createTitledBorder("Force Majeure"));
        c.insets = new Insets(2, 2, 2, 2); //no padding
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        panelRight.add(panelForceMajeure, c);

        // ******************** right panel *******************

        button = new JButton("Import Track");
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        panelRight.add(button, c);

        button = new JButton("Export Track");
        c.gridx = 1;
        c.gridy = 2;
        panelRight.add(button, c);

        c.anchor = GridBagConstraints.NORTH;
        c.weighty = 1;
        c.gridx = 1;
        c.gridy = 1;
        pane.add(panelRight, c);

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

    // public static void main(String[] args) {
    //     //Schedule a job for the event-dispatching thread:
    //     //creating and showing this application's GUI.
    //     javax.swing.SwingUtilities.invokeLater(new Runnable() {
    //         public void run() {
    //             createAndShowGUI();
    //         }
    //     });
    // }
}