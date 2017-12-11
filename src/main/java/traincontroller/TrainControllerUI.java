/*   ______                 _           _
 *  /_  __/ _____  ____ _  (_) ____    (_) ____    ____ _
 *   / /   / ___/ / __ `/ / / / __ \  / / / __ \  / __ `/
 *  / /   / /    / /_/ / / / / / / / / / / / / / / /_/ /
 * /_/   /_/     \__,_/ /_/ /_/ /_/ /_/ /_/ /_/  \__, /
 *     __  ___                 __               /____/
 *    /  |/  / ____    ____   / /_  ____ _  ____ _  ___
 *   / /|_/ / / __ \  / __ \ / __/ / __ `/ / __ `/ / _ \
 *  / /  / / / /_/ / / / / // /_  / /_/ / / /_/ / /  __/
 * /_/  /_/  \____/ /_/ /_/ \__/  \__,_/  \__, /  \___/
 *                                       /____/
 *
 * @author Aric Hudson
 */
package traincontroller;
import java.math.BigDecimal;
import java.math.RoundingMode;
import shared.*;
import trainmodel.Train;
import trackmodel.TrackModel;
import trackmodel.StaticBlock;
import trackmodel.StaticSwitch;
import trackmodel.StaticTrack;

/**
 *
 * @author Didge
 */
public class TrainControllerUI extends javax.swing.JFrame {
    private boolean manualMode = false;
    private int doorstatus = 0;
    
    private TrainController TC;

    /**
     * Creates new form TrainControllerUI
     */
    public TrainControllerUI() {
        initComponents();
    }
    
    public void initialize() {
        TrainController TC = new TrainController();
        initialize(TC);
    }
    
    public void initialize(TrainController newTC) {
        TC = newTC;
        if(TC.checkForTrain()) {
            System.err.println("INIT: there's a train");
//            authField.setText("" + TC.theTrain.getAuthority());
//            authField.setText("" + TC.theMap.distToAuthEnd(TC.distanceTraveled));
            
            currentSpeedField.setText("" + truncDouble(Convert.metersPerSecondToMPH(TC.theTrain.getCurrentVelocity()), 3));
            
//            safeBrakeField.setText("" + truncDouble(TC.computeSafeBrake(), 4));
//            
//            if(TC.computeSafeSpeed() < TC.theTrain.getSuggestedSpeed())
//                setSpeedField.setText("" + truncDouble(TC.computeSafeSpeed(), 4));
//            else
//                setSpeedField.setText("" + truncDouble(TC.theTrain.getSuggestedSpeed(), 4));
//            
//            maxSafeField.setText("" + truncDouble(TC.computeSafeSpeed(), 4));
//            
//            safeBrakeField.setText("" + truncDouble(TC.computeSafeBrake(), 4));
//            
//            suggestedField.setText("" + truncDouble(TC.theTrain.getSuggestedSpeed(), 4));
            
            manOFFButton.setSelected(true);
        }
        else {
            currentSpeedField.setText("No Train");
        }
        
        authField.setText("Not initialized");
            
        safeBrakeField.setText("Not initialized");

        setSpeedField.setText("Not initialized");

        maxSafeField.setText("Not initialized");

        safeBrakeField.setText("Not initialized");
 
        suggestedField.setText("Not initialized");
        
        calcPowerField.setText("" + truncDouble(TC.Pcmd, 3));
            
        currentPowerField.setText("" + truncDouble(TC.Pcmd, 4));
        
        upcomingStationLabel.setText("" + TC.station);
        
        manOFFButton.doClick();
        
        AskForKiKp dialog = new AskForKiKp(new javax.swing.JFrame(), true, TC);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
//        setVisible(true);
    }
    
    public Double truncDouble(double toTrunc, int precision) {
        Double D = new Double(toTrunc);

        Double truncated = BigDecimal.valueOf(D)
            .setScale(precision, RoundingMode.HALF_UP)
            .doubleValue();
        return truncated;
    }
    
    public void updateSpeeds() {
        if(TC.computeSafeSpeed() < TC.theTrain.getSuggestedSpeed())
            setSpeedField.setText("" + truncDouble(Convert.metersPerSecondToMPH(TC.computeSafeSpeed()), 3));
        else
            setSpeedField.setText("" + truncDouble(Convert.metersPerSecondToMPH(TC.theTrain.getSuggestedSpeed()), 3));
        
        currentSpeedField.setText("" + truncDouble(Convert.metersPerSecondToMPH(TC.theTrain.getCurrentVelocity()), 3));
        System.err.println("UPDATESPEED: current speed is " + TC.theTrain.getCurrentVelocity());
        maxSafeField.setText("" + truncDouble(Convert.metersPerSecondToMPH(TC.computeSafeSpeed()), 3));
        safeBrakeField.setText("" + truncDouble(Convert.metersToFeet(TC.computeSafeBrake()), 3));
        suggestedField.setText("" + truncDouble(Convert.metersPerSecondToMPH(TC.theTrain.getSuggestedSpeed()), 3));
        authField.setText("" + truncDouble(Convert.metersToFeet(TC.theMap.distToAuthEnd(TC.distanceTraveled)), 3));
    }
    
    public void updatePowers() {
        if(TC.coast || TC.applyBrakes)
        {
            calcPowerField.setText("0");
            currentPowerField.setText("0");
        }
        else
        {
            calcPowerField.setText("" + TC.Pcmd);
            currentPowerField.setText("" + TC.Pcmd);
        }
    }
    
    public void updateStation(String station) {
        upcomingStationLabel.setText("" + station);
    }
    
    public void updateLights() {
        lightsOFFButton.setSelected(!TC.lightsShouldBeOn);
        lightsONButton.setSelected(TC.lightsShouldBeOn);
    }
    
    public void updateDoors() {
        leftClosedButton.setSelected(!TC.leftShouldBeOpen);
        leftOpenButton.setSelected(TC.leftShouldBeOpen);
        rightClosedButton.setSelected(!TC.rightShouldBeOpen);
        rightOpenButton.setSelected(TC.rightShouldBeOpen);
    }
    
    public void updateAll(){
        updateSpeeds();
        updatePowers();
        updateDoors();
//        updateStation(TC.station);
//        toggleBrake();
        Train_ID.setText("" + TC.getID());
    }
    
    public void toggleBrake() {
        if(TC.theTrain.getCurrentVelocity() == 0)
            applyServiceBrakeButton.setSelected(true);
        else
            applyServiceBrakeButton.setSelected(TC.applyBrakes);
    }
    
    public void switchTrains(TrainController newTC) {
        TC = newTC;
        updateAll();
    }
    
    private void setAllEditable(boolean toSet) {
        authField.setEditable(toSet);
        setSpeedField.setEditable(toSet);
        
        calcPowerField.setEditable(toSet);
        currentPowerField.setEditable(toSet);
        currentSpeedField.setEditable(toSet);
        safeBrakeField.setEditable(toSet);
        setSpeedField.setEditable(toSet);
        maxSafeField.setEditable(toSet);
        safeBrakeField.setEditable(toSet);
        suggestedField.setEditable(toSet);
        tempField.setEditable(toSet);
        
        applyServiceBrakeButton.setEnabled(toSet);
        applyEBrakeButton.setEnabled(toSet);
        
        rightOpenButton.setEnabled(toSet);
        leftOpenButton.setEnabled(toSet);
        rightClosedButton.setEnabled(toSet);
        leftClosedButton.setEnabled(toSet);
        
        lightsONButton.setEnabled(toSet);
        lightsOFFButton.setEnabled(toSet);
        
//        seeTrainsButton.setEnabled(toSet);
        setSpeedButton.setEnabled(toSet);
        setTempButton.setEnabled(toSet);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        applyBrakeButton1 = new javax.swing.JToggleButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        authField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        authMiles = new javax.swing.JLabel();
        Train_ID = new javax.swing.JLabel();
        seeTrainsButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        upcomingStationLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        currentSpeedField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        setSpeedButton = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        suggestedField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        setSpeedField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        safeBrakeField = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        maxSafeField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        calcPowerField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        applyServiceBrakeButton = new javax.swing.JToggleButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        currentPowerField = new javax.swing.JTextField();
        applyEBrakeButton = new javax.swing.JToggleButton();
        jPanel5 = new javax.swing.JPanel();
        manONButton = new javax.swing.JToggleButton();
        manOFFButton = new javax.swing.JToggleButton();
        jPanel6 = new javax.swing.JPanel();
        lightsONButton = new javax.swing.JToggleButton();
        lightsOFFButton = new javax.swing.JToggleButton();
        jPanel7 = new javax.swing.JPanel();
        rightOpenButton = new javax.swing.JToggleButton();
        rightClosedButton = new javax.swing.JToggleButton();
        jPanel8 = new javax.swing.JPanel();
        leftOpenButton = new javax.swing.JToggleButton();
        leftClosedButton = new javax.swing.JToggleButton();
        jPanel9 = new javax.swing.JPanel();
        tempField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        setTempButton = new javax.swing.JButton();

        jLabel4.setText("jLabel4");

        applyBrakeButton1.setText("BRAKE");
        applyBrakeButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyBrakeButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("jLabel3");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Train Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13))); // NOI18N

        jLabel1.setText("Train ID:");

        authField.setText("X");
        authField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                authFieldActionPerformed(evt);
            }
        });

        jLabel2.setText("Authority:");

        authMiles.setText("Feet");

        Train_ID.setText("0");

        seeTrainsButton.setText("See All Trains");
        seeTrainsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seeTrainsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Train_ID)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(authField, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(authMiles)
                .addGap(18, 18, 18)
                .addComponent(seeTrainsButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(Train_ID)
                    .addComponent(authField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(authMiles)
                    .addComponent(seeTrainsButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Station", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13))); // NOI18N

        upcomingStationLabel.setFont(new java.awt.Font("Lucida Grande", 1, 20)); // NOI18N
        upcomingStationLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        upcomingStationLabel.setText("UPCOMING STATION INFORMATION");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(upcomingStationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(upcomingStationLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Velocity Feedback", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13))); // NOI18N

        jLabel17.setText("mph");

        currentSpeedField.setText("XX");
        currentSpeedField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentSpeedFieldActionPerformed(evt);
            }
        });

        jLabel6.setText("Current Train Speed:");

        setSpeedButton.setText("Set");

        jLabel16.setText("mph");

        suggestedField.setText("XX");
        suggestedField.setActionCommand("<Not Set>");
        suggestedField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suggestedFieldActionPerformed(evt);
            }
        });

        jLabel5.setText("Suggested Speed:");

        jLabel19.setText("mph");

        setSpeedField.setText("XX");
        setSpeedField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setSpeedFieldActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel8.setText("Set Speed:");

        jLabel12.setText("Current Safe Braking Distance:");

        safeBrakeField.setText("XX");

        jLabel23.setText("Feet");

        jLabel18.setText("mph");

        maxSafeField.setText("XX");
        maxSafeField.setToolTipText("");
        maxSafeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxSafeFieldActionPerformed(evt);
            }
        });

        jLabel7.setText("Maximum Safe Speed:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(suggestedField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(maxSafeField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(currentSpeedField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel18)
                            .addComponent(jLabel17)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(setSpeedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(setSpeedButton))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(safeBrakeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel23)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentSpeedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(maxSafeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(suggestedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(safeBrakeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(setSpeedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel19)
                    .addComponent(setSpeedButton))
                .addGap(30, 30, 30))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Power Feedback", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13))); // NOI18N

        jLabel20.setText("W");

        calcPowerField.setText("XX");
        calcPowerField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcPowerFieldActionPerformed(evt);
            }
        });

        jLabel9.setText("Calculated Power:");

        applyServiceBrakeButton.setText("SERVICE BRAKE");
        applyServiceBrakeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyServiceBrakeButtonActionPerformed(evt);
            }
        });

        jLabel15.setText("Current Power:");

        jLabel22.setText("W");

        currentPowerField.setText("XX");
        currentPowerField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentPowerFieldActionPerformed(evt);
            }
        });

        applyEBrakeButton.setText("E BRAKE");
        applyEBrakeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyEBrakeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(25, 25, 25)
                        .addComponent(currentPowerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(calcPowerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel20)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(applyServiceBrakeButton)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(applyEBrakeButton)
                        .addGap(37, 37, 37))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentPowerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel22)
                    .addComponent(applyServiceBrakeButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(calcPowerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel20)
                    .addComponent(applyEBrakeButton))
                .addGap(35, 35, 35))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Manual Mode", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13))); // NOI18N

        manONButton.setText("ON");
        manONButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manONButtonActionPerformed(evt);
            }
        });

        manOFFButton.setText("OFF");
        manOFFButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manOFFButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(manONButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(manOFFButton, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(manONButton)
                    .addComponent(manOFFButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lights", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13))); // NOI18N

        lightsONButton.setText("ON");
        lightsONButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lightsONButtonActionPerformed(evt);
            }
        });

        lightsOFFButton.setText("OFF");
        lightsOFFButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lightsOFFButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(lightsONButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addComponent(lightsOFFButton, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lightsONButton)
                    .addComponent(lightsOFFButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Right Doors", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13))); // NOI18N

        rightOpenButton.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        rightOpenButton.setText("OPEN");
        rightOpenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rightOpenButtonActionPerformed(evt);
            }
        });

        rightClosedButton.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        rightClosedButton.setText("CLOSED");
        rightClosedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rightClosedButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(rightOpenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rightClosedButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rightOpenButton)
                    .addComponent(rightClosedButton))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Left Doors", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13))); // NOI18N

        leftOpenButton.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        leftOpenButton.setText("OPEN");
        leftOpenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftOpenButtonActionPerformed(evt);
            }
        });

        leftClosedButton.setFont(new java.awt.Font("Lucida Grande", 0, 10)); // NOI18N
        leftClosedButton.setText("CLOSED");
        leftClosedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftClosedButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(leftOpenButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(leftClosedButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(leftOpenButton)
                    .addComponent(leftClosedButton))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Temperture", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13))); // NOI18N

        tempField.setText("72");
        tempField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tempFieldActionPerformed(evt);
            }
        });

        jLabel13.setText("Temp:");

        jLabel14.setText("°");

        setTempButton.setText("Set");
        setTempButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setTempButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tempField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(setTempButton))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tempField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(setTempButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void authFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_authFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_authFieldActionPerformed

    private void manOFFButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manOFFButtonActionPerformed
        manualMode = false;
        manONButton.setSelected(false);
        setAllEditable(false);
    }//GEN-LAST:event_manOFFButtonActionPerformed

    private void suggestedFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suggestedFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_suggestedFieldActionPerformed

    private void currentSpeedFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentSpeedFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_currentSpeedFieldActionPerformed

    private void maxSafeFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_maxSafeFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_maxSafeFieldActionPerformed

    private void setSpeedFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setSpeedFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_setSpeedFieldActionPerformed

    private void calcPowerFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calcPowerFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_calcPowerFieldActionPerformed

    private void applyServiceBrakeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyServiceBrakeButtonActionPerformed
//        TC.applyBrakes = !TC.applyBrakes;
//        applyServiceBrakeButton.setSelected(!applyServiceBrakeButton.isSelected());
        if(applyServiceBrakeButton.isSelected())
        {
            TC.justStop();
        }
        else if(!applyEBrakeButton.isSelected())
        {
            TC.youCanGoNow();
        }
    }//GEN-LAST:event_applyServiceBrakeButtonActionPerformed

    private void currentPowerFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentPowerFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_currentPowerFieldActionPerformed

    private void lightsOFFButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lightsOFFButtonActionPerformed
        TC.setLights(false);
        lightsONButton.setSelected(false);
    }//GEN-LAST:event_lightsOFFButtonActionPerformed

    private void rightClosedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightClosedButtonActionPerformed
        doorstatus -= 1;
        TC.setDoors(doorstatus);
        rightOpenButton.setSelected(false);
    }//GEN-LAST:event_rightClosedButtonActionPerformed

    private void leftClosedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftClosedButtonActionPerformed
        doorstatus -= 2;
        TC.setDoors(doorstatus);
        leftOpenButton.setSelected(false);
    }//GEN-LAST:event_leftClosedButtonActionPerformed

    private void applyBrakeButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyBrakeButton1ActionPerformed
//        TC.applyBrakes = !TC.applyBrakes;
//        applyBrakeButton.setSelected(!applyBrakeButton.isSelected());
    }//GEN-LAST:event_applyBrakeButton1ActionPerformed

    private void applyEBrakeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyEBrakeButtonActionPerformed
        if(applyServiceBrakeButton.isSelected())
        {
            TC.justStop();
        }
        else if(!applyEBrakeButton.isSelected())
        {
            TC.youCanGoNow();
        } 
    }//GEN-LAST:event_applyEBrakeButtonActionPerformed

    private void manONButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manONButtonActionPerformed
        manualMode = true;
        manOFFButton.setSelected(false);
        setAllEditable(true);
    }//GEN-LAST:event_manONButtonActionPerformed

    private void rightOpenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightOpenButtonActionPerformed
        if(TC.theTrain.getCurrentVelocity() == 0)
        {
            doorstatus += 1;
            System.err.println("Door status: " + doorstatus);
            TC.setDoors(doorstatus);
            rightClosedButton.setSelected(false);
        }
        else
        {
            System.err.println("Cannot open doors while train is in motion");
            rightOpenButton.setSelected(false);
        }
    }//GEN-LAST:event_rightOpenButtonActionPerformed

    private void lightsONButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lightsONButtonActionPerformed
        TC.setLights(true);
        lightsOFFButton.setSelected(false);
    }//GEN-LAST:event_lightsONButtonActionPerformed

    private void leftOpenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftOpenButtonActionPerformed
        if(TC.theTrain.getCurrentVelocity() == 0)
        {
            doorstatus += 2;
            System.err.println("Door status: " + doorstatus);
            TC.setDoors(doorstatus);
            leftClosedButton.setSelected(false);
        }
        else
        {
            System.err.println("Cannot open doors while train is in motion");
            leftOpenButton.setSelected(false);
        }
    }//GEN-LAST:event_leftOpenButtonActionPerformed

    private void tempFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tempFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tempFieldActionPerformed

    private void seeTrainsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seeTrainsButtonActionPerformed
        TC.listUI.setVisible(true);
    }//GEN-LAST:event_seeTrainsButtonActionPerformed

    private void setTempButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setTempButtonActionPerformed
        TC.setTemp(Double.parseDouble(tempField.getText()));
    }//GEN-LAST:event_setTempButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TrainControllerUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TrainControllerUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TrainControllerUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TrainControllerUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TrainControllerUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Train_ID;
    private javax.swing.JToggleButton applyBrakeButton1;
    private javax.swing.JToggleButton applyEBrakeButton;
    private javax.swing.JToggleButton applyServiceBrakeButton;
    private javax.swing.JTextField authField;
    private javax.swing.JLabel authMiles;
    private javax.swing.JTextField calcPowerField;
    private javax.swing.JTextField currentPowerField;
    private javax.swing.JTextField currentSpeedField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JToggleButton leftClosedButton;
    private javax.swing.JToggleButton leftOpenButton;
    private javax.swing.JToggleButton lightsOFFButton;
    private javax.swing.JToggleButton lightsONButton;
    private javax.swing.JToggleButton manOFFButton;
    private javax.swing.JToggleButton manONButton;
    private javax.swing.JTextField maxSafeField;
    private javax.swing.JToggleButton rightClosedButton;
    private javax.swing.JToggleButton rightOpenButton;
    private javax.swing.JTextField safeBrakeField;
    private javax.swing.JButton seeTrainsButton;
    private javax.swing.JButton setSpeedButton;
    private javax.swing.JTextField setSpeedField;
    private javax.swing.JButton setTempButton;
    private javax.swing.JTextField suggestedField;
    private javax.swing.JTextField tempField;
    private javax.swing.JLabel upcomingStationLabel;
    // End of variables declaration//GEN-END:variables
}
