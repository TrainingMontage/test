package CTCModel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class CTCGUI {
    // dummy modules that the GUI needs to interact with
    private static TrackModel trackModel;
    private static TrainModel trainModel;
    // private train list
    private static boolean dataValid;
    private static int dataTrainID;
    private static int dataBlockID;
    private static int dataSpeed;
    private static String dataAuthority;
    private static int dataOrigin;
    private static int dataDestination;
    private static JLabel trainLabel;
    // private env temperature
    private static double temperature;
    // constants for initializing GUI components
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    final static int TextAreaWidth = 80;
    final static int TextAreaHeight = 16;
    // menu handle (needs to be added in createAndShowGUI)
    private static JMenuBar menu;
    // GUI button handles
    private static JButton manualButton;
    private static JButton autoButton;
    private static JButton newTrainButton;
    private static JButton launchTrainButton;
    // train textArea handles
    private static JTextArea trainIDText;
    private static JTextArea trainBlockText;
    private static JTextArea trainSpeedText;
    private static JTextArea trainAuthorityText;
    private static JTextArea trainOriginText;
    private static JTextArea trainDestinationText;
    // track textArea handles
    private static JTextArea trackIDText;
    private static JTextArea trackOccupiedText;
    private static JTextArea trackSpeedText;
    private static JTextArea trackLengthText;
    private static JTextArea trackGradeText;
    private static JTextArea trackElevationText;
    private static JTextArea trackPassableText;
    private static JTextArea trackHeaterText;
    private static JTextArea trackUndergroundText;
    private static JTextArea trackLightText;
    private static JTextArea trackSwitchText;
    private static JTextArea trackStationText;
    private static JTextArea trackPassengersText;
    private static JTextArea trackCrossingText;
    
    //has the user pushed the new train button but not yet launched it
    private static boolean isNewTrain;
    
    CTCGUI(TrackModel atrackModel, TrainModel atrainModel){
        trackModel = atrackModel;
        trainModel = atrainModel;
    }
    
    public static void addComponentsToPane(Container pane){
        if(RIGHT_TO_LEFT){
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
        isNewTrain = false;
        dataValid = false;
        temperature = 70.0;
        
        JButton button;
        JLabel label;
        JTextArea textArea;
        JCheckBox checkBox;
        JComboBox comboBox;
        
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        if(shouldFill){
            //natural height, maximum width
            c.fill = GridBagConstraints.HORIZONTAL;
        }
        c.weightx = 0.5;
        c.weighty = 0.5;
        
        //menu declarations
        menu = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        //JMenuItem menuCreateTrain = new JMenuItem("Create Train");
        JMenuItem menuUploadSchedule = new JMenuItem("Upload Schedule");
        //TODO: more menu items?
        
        //sub-panel declarations
        JPanel panelCTCInfo = new JPanel();
        JPanel panelTrainInfo = new JPanel();
        JPanel panelTrackInfo = new JPanel();
        JPanel panelSchedule = new JPanel();
        JPanel panelTrackGraph = new JPanel();
        
        //new Dimension(width,height)
        panelCTCInfo.setPreferredSize(new Dimension(300,62));
        panelTrainInfo.setPreferredSize(new Dimension(300,175));
        panelTrackInfo.setPreferredSize(new Dimension(300,310));
        panelSchedule.setPreferredSize(new Dimension(600,300));
        panelTrackGraph.setPreferredSize(new Dimension(700,643));
        
        // -------------------- menu bar --------------------
        //menuCreateTrain.addActionListener(new ActionListener() {
        //    public void actionPerformed(ActionEvent ae) {
        //        
        //    }
        //});
        //menuFile.add(menuCreateTrain);
        menuFile.add(menuUploadSchedule);
        menu.add(menuFile);
        //added to frame in createAndShowGUI
        
        // -------------------- main panel --------------------
        label = new JLabel("CTC GUI");
        c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        pane.add(label,c);
        
        label = new JLabel("8:00 AM");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 1;
        //c.gridy = 0;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        pane.add(label,c);
        
        manualButton = new JButton("Manual");
        manualButton.setEnabled(false);
        manualButton.setBackground(Color.GREEN);
        //manualButton.setOpaque(true);
        manualButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                manualButton.setEnabled(false);
                autoButton.setEnabled(true);
                manualButton.setBackground(Color.GREEN);
                //manualButton.setOpaque(true);
                autoButton.setBackground(new JButton().getBackground());
                newTrainButton.setEnabled(true);
                if(isNewTrain){
                    //enable the text boxes
                    trainBlockText.setEnabled(true);
                    trainSpeedText.setEnabled(true);
                    trainAuthorityText.setEnabled(true);
                    trainDestinationText.setEnabled(true);
                    //enable the submit button
                    launchTrainButton.setEnabled(true);
                }
            }
        });
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 2;
        //c.gridy = 0;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        pane.add(manualButton,c);
        
        autoButton = new JButton("Automatic");
        autoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                autoButton.setEnabled(false);
                manualButton.setEnabled(true);
                autoButton.setBackground(Color.GREEN);
                //autoButton.setOpaque(true);
                manualButton.setBackground(new JButton().getBackground());
                newTrainButton.setEnabled(false);
                if(isNewTrain){
                    //disable the text boxes
                    trainBlockText.setEnabled(false);
                    trainSpeedText.setEnabled(false);
                    trainAuthorityText.setEnabled(false);
                    trainDestinationText.setEnabled(false);
                    //disable the submit button
                    launchTrainButton.setEnabled(false);
                }
            }
        });
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 3;
        //c.gridy = 0;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        pane.add(autoButton,c);
        
        // -------------------- CTC panel --------------------
        panelCTCInfo.setLayout(new GridBagLayout());
        
        
        label = new JLabel("Temperature");
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelCTCInfo.add(label,c);
        
        label = new JLabel("Throughput");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 1;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelCTCInfo.add(label,c);
        
        textArea = new JTextArea("70°F");
        textArea.setEnabled(false);
        textArea.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 1;
        c.gridy = 0;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelCTCInfo.add(textArea,c);
        
        textArea = new JTextArea("5000 people");
        textArea.setEnabled(false);
        textArea.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 1;
        c.gridy = 1;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelCTCInfo.add(textArea,c);
        
        panelCTCInfo.setBorder(BorderFactory.createTitledBorder("CTC Info"));
        c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 1;
        pane.add(panelCTCInfo,c);
        
        // -------------------- Train panel --------------------
        panelTrainInfo.setLayout(new GridBagLayout());
        
        label = new JLabel("Train ID");
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrainInfo.add(label,c);
        
        label = new JLabel("Current Block");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 1;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(label,c);
        
        label = new JLabel("Suggested Speed");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 2;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(label,c);
        
        label = new JLabel("Given Authority");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 3;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(label,c);
        
        label = new JLabel("Origin");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 4;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(label,c);
        
        label = new JLabel("Destination");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 5;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(label,c);
        
        trainIDText = new JTextArea("10");
        trainIDText.setEnabled(false);
        trainIDText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 1;
        c.gridy = 0;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(trainIDText,c);
        
        trainBlockText = new JTextArea("2");
        trainBlockText.setEnabled(false);
        trainBlockText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 1;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(trainBlockText,c);
        
        trainSpeedText = new JTextArea("25 mph");
        trainSpeedText.setEnabled(false);
        trainSpeedText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 2;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(trainSpeedText,c);
        
        trainAuthorityText = new JTextArea("2");
        trainAuthorityText.setEnabled(false);
        trainAuthorityText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 3;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(trainAuthorityText,c);
        
        trainOriginText = new JTextArea("Shadyside");
        trainOriginText.setEnabled(false);
        trainOriginText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 4;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(trainOriginText,c);
        
        trainDestinationText = new JTextArea("Edgebrook");
        trainDestinationText.setEnabled(false);
        trainDestinationText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 5;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(trainDestinationText,c);
        
        newTrainButton = new JButton("New Train");
        newTrainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                //clear the text boxes
                trainIDText.setText("");
                trainBlockText.setText("");
                trainSpeedText.setText("");
                trainAuthorityText.setText("");
                trainOriginText.setText("");
                trainDestinationText.setText("");
                //enable the text boxes
                trainBlockText.setEnabled(true);
                trainSpeedText.setEnabled(true);
                trainAuthorityText.setEnabled(true);
                trainDestinationText.setEnabled(true);
                //enable the submit button
                launchTrainButton.setEnabled(true);
                isNewTrain = true;
            }
        });
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 6;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(newTrainButton,c);
        
        launchTrainButton = new JButton("Launch Train");
        launchTrainButton.setEnabled(false);
        launchTrainButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                //reset all borders
                Border border = new JTextArea("").getBorder();
                trainBlockText.setBorder(border);
                trainSpeedText.setBorder(border);
                trainAuthorityText.setBorder(border);
                trainDestinationText.setBorder(border);
                //check text
                int error = checkTrainInputs(trainBlockText.getText(),
                                             trainSpeedText.getText(),
                                             trainAuthorityText.getText(),
                                             trainDestinationText.getText());
                switch(error){
                case 0:
                    //success
                    //disable text boxes
                    trainBlockText.setEnabled(false);
                    trainSpeedText.setEnabled(false);
                    trainAuthorityText.setEnabled(false);
                    trainDestinationText.setEnabled(false);
                    //send to createTrain
                    trainModel.createTrain(Integer.parseInt(trainBlockText.getText()));
                    //update own train data
                    dataValid = true;
                    dataTrainID = 1;
                    dataBlockID = Integer.parseInt(trainBlockText.getText());
                    dataSpeed = Integer.parseInt(trainSpeedText.getText());
                    dataAuthority = trainAuthorityText.getText();
                    dataOrigin = dataBlockID;
                    dataDestination = Integer.parseInt(trainDestinationText.getText());
                    trainLabel.setText("Train");
                    //disable the submit button
                    launchTrainButton.setEnabled(false);
                    isNewTrain = false;
                    fillTrackInfo(dataBlockID);
                    break;
                case 1:
                    trainBlockText.setBorder(BorderFactory.createLineBorder(Color.RED));
                    break;
                case 2:
                    trainSpeedText.setBorder(BorderFactory.createLineBorder(Color.RED));
                    break;
                case 3:
                    trainAuthorityText.setBorder(BorderFactory.createLineBorder(Color.RED));
                    break;
                case 4:
                    trainDestinationText.setBorder(BorderFactory.createLineBorder(Color.RED));
                    break;
                default:
                    //unknown error; do nothing
                    break;
                }
            }
        });
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 1;
        c.gridy = 6;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrainInfo.add(launchTrainButton,c);
        
        panelTrainInfo.setBorder(BorderFactory.createTitledBorder("Train Panel"));
        c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 1;
        pane.add(panelTrainInfo,c);
        
        // -------------------- Track panel --------------------
        panelTrackInfo.setLayout(new GridBagLayout());
        
        label = new JLabel("Block ID");
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Occupied State");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 1;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Speed Limit");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 2;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Length");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 3;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Grade");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 4;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Elevation");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 5;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Block Passable");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 6;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Heater State");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 7;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Underground");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 8;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Light State");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 9;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Switch State");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 10;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Station Name");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 11;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Waiting Passengers");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 12;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        label = new JLabel("Crossing State");
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 0;
        c.gridy = 13;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(label,c);
        
        trackIDText = new JTextArea("9");
        trackIDText.setEnabled(false);
        trackIDText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 1;
        c.gridy = 0;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackIDText,c);
        
        trackOccupiedText = new JTextArea("Occupied");
        trackOccupiedText.setEnabled(false);
        trackOccupiedText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 1;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackOccupiedText,c);
        
        trackSpeedText = new JTextArea("5 mph");
        trackSpeedText.setEnabled(false);
        trackSpeedText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 2;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackSpeedText,c);
        
        trackLengthText = new JTextArea("1000 ft");
        trackLengthText.setEnabled(false);
        trackLengthText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 3;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackLengthText,c);
        
        trackGradeText = new JTextArea("0.5%");
        trackGradeText.setEnabled(false);
        trackGradeText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 4;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackGradeText,c);
        
        trackElevationText = new JTextArea("100 ft");
        trackElevationText.setEnabled(false);
        trackElevationText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 5;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackElevationText,c);
        
        trackPassableText = new JTextArea("Passable");
        trackPassableText.setEnabled(false);
        trackPassableText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 6;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackPassableText,c);
        
        trackHeaterText = new JTextArea("No Heater");
        trackHeaterText.setEnabled(false);
        trackHeaterText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 7;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackHeaterText,c);
        
        trackUndergroundText = new JTextArea("Above Ground");
        trackUndergroundText.setEnabled(false);
        trackUndergroundText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 8;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackUndergroundText,c);
        
        trackLightText = new JTextArea("No Lights");
        trackLightText.setEnabled(false);
        trackLightText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 9;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackLightText,c);
        
        trackSwitchText = new JTextArea("No Switch");
        trackSwitchText.setEnabled(false);
        trackSwitchText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 10;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackSwitchText,c);
        
        trackStationText = new JTextArea("Shadyside");
        trackStationText.setEnabled(false);
        trackStationText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 11;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackStationText,c);
        
        trackPassengersText = new JTextArea("50");
        trackPassengersText.setEnabled(false);
        trackPassengersText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 12;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackPassengersText,c);
        
        trackCrossingText = new JTextArea("No Crossing");
        trackCrossingText.setEnabled(false);
        trackCrossingText.setPreferredSize(new Dimension(TextAreaWidth,TextAreaHeight));
        //c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        //c.gridx = 1;
        c.gridy = 13;
        //c.gridwidth = 1;
        //c.gridheight = 1;
        panelTrackInfo.add(trackCrossingText,c);
        
        panelTrackInfo.setBorder(BorderFactory.createTitledBorder("Track Panel"));
        c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 2;
        pane.add(panelTrackInfo,c);
        
        // -------------------- Schedule panel --------------------
        panelSchedule.setLayout(new GridBagLayout());
        
        panelSchedule.setBorder(BorderFactory.createTitledBorder("Schedule Panel"));
        c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 4;
        c.gridheight = 1;
        pane.add(panelSchedule,c);
        
        // -------------------- Map panel --------------------
        panelTrackGraph.setLayout(new GridBagLayout());
        
        label = new JLabel("");
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(label,c);
        
        label = new JLabel("");
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(label,c);
        
        label = new JLabel("");
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(label,c);
        
        trainLabel = new JLabel(" ", SwingConstants.CENTER);
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(trainLabel,c);
        
        label = new JLabel("Yard", SwingConstants.CENTER);
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(label,c);
        
        button = new JButton("=|=|=|=");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                fillTrackInfo(0);
            }
        });
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(button,c);
        
        button = new JButton("=|=|=|=");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                fillTrackInfo(1);
            }
        });
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 2;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(button,c);
        
        button = new JButton("=|=|=|=");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                fillTrackInfo(2);
            }
        });
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 3;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(button,c);
        
        button = new JButton("=|=|=|=");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                fillTrackInfo(3);
            }
        });
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 4;
        c.gridy = 4;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(button,c);
        
        label = new JLabel("");
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(label,c);
        
        label = new JLabel("");
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(label,c);
        
        label = new JLabel("");
        //label.setPreferredSize(new Dimension(75,16));
        c.insets = new Insets(0,0,0,0);//top,left,bottom,right
        c.gridx = 0;
        c.gridy = 7;
        c.gridwidth = 1;
        c.gridheight = 1;
        panelTrackGraph.add(label,c);
        
        panelTrackGraph.setBorder(BorderFactory.createTitledBorder("Map Panel"));
        c.insets = new Insets(2,2,2,2);//top,left,bottom,right
        c.gridx = 4;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 4;
        pane.add(panelTrackGraph,c);
    }
    
    static void fillTrackInfo(int blockID){
        //fill static info
        StaticBlock staticBlock = trackModel.getStaticBlock(blockID);
        trackIDText.setText("" + staticBlock.getBlockID());
        trackSpeedText.setText("" + staticBlock.getSpeedLimit() + " mph");
        trackLengthText.setText("" + staticBlock.getLength() + " ft");
        trackGradeText.setText("" + staticBlock.getGrade() + "%");
        trackElevationText.setText("" + staticBlock.getElevation() + " ft");
        trackUndergroundText.setText("" + staticBlock.isUnderground());
        boolean hasLight = staticBlock.hasLight();
        boolean hasSwitch = staticBlock.hasSwitch();
        boolean hasRailway = staticBlock.hasRailway();
        boolean hasHeater = staticBlock.hasHeater();
        if(!hasLight){
            trackLightText.setText("No Lights");
        }
        if(!hasSwitch){
            trackSwitchText.setText("No Switch");
        }
        if(!staticBlock.hasStation()){
            trackStationText.setText("No Station");
            trackPassengersText.setText("");
        }else{
            trackStationText.setText(staticBlock.getStationName());
            trackPassengersText.setText("0");
        }
        if(!hasRailway){
            trackCrossingText.setText("No Railway Crossing");
        }
        if(!hasHeater){
            trackHeaterText.setText("No Heater");
        }
        
        //fill dynamic info
        if(trackModel.isOccupied(blockID)){
            trackOccupiedText.setText("Occupied");
        }else{
            trackOccupiedText.setText("Not Occupied");
        }
        switch(trackModel.getStatus(blockID)){
        case 0:
            trackPassableText.setText("Passable");
            break;
        case 1:
            trackPassableText.setText("Broken");
            break;
        case 2:
            trackPassableText.setText("Maintenance");
            break;
        default:
            //unknown status; leave blank
            trackPassableText.setText("");
            break;
        }
        if(hasLight){
            if(trackModel.getSignal(blockID)){
                trackLightText.setText("Green");
            }else{
                trackLightText.setText("Red");
            }
        }
        if(hasSwitch){
            if(trackModel.getSwitch(blockID)){
                trackSwitchText.setText("North");
            }else{
                trackSwitchText.setText("South");
            }
        }
        if(hasRailway){
            if(trackModel.getCrossing(blockID)){
                trackCrossingText.setText("Active");
            }else{
                trackCrossingText.setText("Inactive");
            }
        }
        if(hasHeater){
            if(temperature <= 32){
                trackHeaterText.setText("Heater Active");
            }else{
                trackHeaterText.setText("Heater Inactive");
            }
        }
        
        //if there is a train there, fill train info
        isNewTrain = false;
        if(blockID == dataBlockID && dataValid){
            trainIDText.setText("" + dataTrainID);
            trainBlockText.setText("" + dataBlockID);
            trainSpeedText.setText("" + dataSpeed + " mph");
            trainAuthorityText.setText(dataAuthority);
            trainOriginText.setText("" + dataOrigin);
            trainDestinationText.setText("" + dataDestination);
        }else{
            //blank everything
            if(!isNewTrain){
                trainIDText.setText("");
                trainBlockText.setText("");
                trainSpeedText.setText("");
                trainAuthorityText.setText("");
                trainOriginText.setText("");
                trainDestinationText.setText("");
            }
        }
        
    }
    
    static int checkTrainInputs(String block, String speed, String authority, String destination){
        int tempint;
        //block number test
        try{
            tempint = Integer.parseInt(block);
        }catch(NumberFormatException ex){
            return 1;
        }
        if(tempint != 0){
            return 1;
        }
        //speed test
        try{
            tempint = Integer.parseInt(speed);
        }catch(NumberFormatException ex){
            return 2;
        }
        //authority test
        //FIXME: fix this up; list of blocks
        try{
            tempint = Integer.parseInt(authority);
        }catch(NumberFormatException ex){
            return 3;
        }
        if(tempint < 0 || tempint > 5){
            return 3;
        }
        //destination test
        try{
            tempint = Integer.parseInt(destination);
        }catch(NumberFormatException ex){
            return 4;
        }
        if(tempint < 0 || tempint > 3){
            return 4;
        }
        return 0;
    }
    
    public static void createAndShowGUI() {
        try{
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }catch(Exception e){
            e.printStackTrace(); 
        }
        UIManager.put("TextArea.inactiveForeground", Color.DARK_GRAY);
        //TODO: figure out why I can't set the background
        //UIManager.put("TextArea.inactiveBackground", Color.LIGHT_GRAY);
        //UIManager.put("TextArea.inactiveForeground", Color.DARK_GRAY);
        
        //Create and set up the window.
        JFrame frame = new JFrame("CTC GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());
        frame.setJMenuBar(menu);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}