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
            
            currentSpeedField.setText("" + truncDouble(TC.theTrain.getCurrentVelocity(), 4));
            
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
        {//else {
            authField.setText("No Train");
            
            currentSpeedField.setText("No Train");
            
            safeBrakeField.setText("No Train");
            
            setSpeedField.setText("No Train");
            
            maxSafeField.setText("No Train");
            
            safeBrakeField.setText("No Train");
            
            suggestedField.setText("No Train");
        }
        
        calcPowerField.setText("" + truncDouble(TC.Pcmd, 4));
            
        currentPowerField.setText("" + truncDouble(TC.Pcmd, 4));
        
        setPowerField.setText("" + truncDouble(TC.Pcmd, 4));
        
        upcomingStationLabel.setText("" + TC.station);
        
        authField.setEditable(false);
        calcPowerField.setEditable(false);
        currentPowerField.setEditable(false);
        currentSpeedField.setEditable(false);
        setPowerField.setEditable(false);
        safeBrakeField.setEditable(false);
        setSpeedField.setEditable(false);
        maxSafeField.setEditable(false);
        safeBrakeField.setEditable(false);
        suggestedField.setEditable(false);
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
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
            setSpeedField.setText("" + TC.computeSafeSpeed());
        else
            setSpeedField.setText("" + TC.theTrain.getSuggestedSpeed());
        
        currentSpeedField.setText("" + TC.theTrain.getCurrentVelocity());
        System.err.println("UPDATESPEED: current speed is " + TC.theTrain.getCurrentVelocity());
        maxSafeField.setText("" + TC.computeSafeSpeed());
        safeBrakeField.setText("" + TC.computeSafeBrake());
        suggestedField.setText("" + TC.theTrain.getSuggestedSpeed());
    }
    
    public void updatePowers() {
        if(TC.coast || TC.applyBrakes)
        {
            setPowerField.setText("0");
            calcPowerField.setText("0");
            currentPowerField.setText("0");
        }
        else
        {
            setPowerField.setText("" + TC.Pcmd);
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
        toggleBrake();
    }
    
    public void toggleBrake() {
        if(TC.theTrain.getCurrentVelocity() == 0)
            applyBrakeButton.setSelected(true);
        else
            applyBrakeButton.setSelected(TC.applyBrakes);
    }
    
    public void switchTrains(TrainController newTC) {
        TC = newTC;
        updateAll();
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
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        TrainList = new javax.swing.JComboBox<>();
        authField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        authMiles = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        upcomingStationLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        currentSpeedField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        setSuggestedButton = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        suggestedField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        setSpeedField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        getPowerButton = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        safeBrakeField = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        maxSafeField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        setMaxSafeButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        setPowerField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        calcPowerField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        applyBrakeButton = new javax.swing.JToggleButton();
        jLabel15 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        currentPowerField = new javax.swing.JTextField();
        applyBrakeButton2 = new javax.swing.JToggleButton();
        jPanel5 = new javax.swing.JPanel();
        manONButton = new javax.swing.JToggleButton();
        manOFFButton = new javax.swing.JToggleButton();
        manChangesButton = new javax.swing.JButton();
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
        jTextField1 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        jLabel4.setText("jLabel4");

        applyBrakeButton1.setText("BRAKE");
        applyBrakeButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyBrakeButton1ActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Train Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13))); // NOI18N

        jLabel1.setText("Train ID:");

        TrainList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Train 1", "Train 2", "Train 3", "Train 4" }));

        authField.setText("X");
        authField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                authFieldActionPerformed(evt);
            }
        });

        jLabel2.setText("Authority:");

        authMiles.setText("Mi");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(TrainList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(authField, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(authMiles)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(authField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addComponent(authMiles))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(TrainList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
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

        setSuggestedButton.setText("Set");

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

        getPowerButton.setText("Get Power");
        getPowerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getPowerButtonActionPerformed(evt);
            }
        });

        jLabel12.setText("Current Safe Braking Distance:");

        safeBrakeField.setText("XX");

        jLabel23.setText("miles");

        jLabel18.setText("mph");

        maxSafeField.setText("XX");
        maxSafeField.setToolTipText("");
        maxSafeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                maxSafeFieldActionPerformed(evt);
            }
        });

        jLabel7.setText("Maximum Safe Speed:");

        setMaxSafeButton.setText("Set");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(setSpeedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(getPowerButton))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(safeBrakeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel23))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(suggestedField, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                            .addComponent(maxSafeField, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(currentSpeedField, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel18)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(setMaxSafeButton))
                            .addComponent(jLabel17)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(setSuggestedButton)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(jLabel18)
                    .addComponent(setMaxSafeButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(suggestedField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel16)
                    .addComponent(setSuggestedButton))
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
                    .addComponent(getPowerButton))
                .addGap(30, 30, 30))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Power Feedback", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13))); // NOI18N

        setPowerField.setText("XX");
        setPowerField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setPowerFieldActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel10.setText("Set Power");

        jLabel20.setText("W");

        jLabel21.setText("W");

        calcPowerField.setText("XX");
        calcPowerField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcPowerFieldActionPerformed(evt);
            }
        });

        jLabel9.setText("Calculated Power:");

        applyBrakeButton.setText("SERVICE BRAKE");
        applyBrakeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyBrakeButtonActionPerformed(evt);
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

        applyBrakeButton2.setText("E BRAKE");
        applyBrakeButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyBrakeButton2ActionPerformed(evt);
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
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(calcPowerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(setPowerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel20))))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(applyBrakeButton)
                        .addContainerGap(12, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(applyBrakeButton2)
                        .addGap(39, 39, 39))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentPowerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel22)
                    .addComponent(applyBrakeButton))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(calcPowerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel20))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(setPowerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel21))
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(applyBrakeButton2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Manual Mode", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13))); // NOI18N

        manONButton.setText("ON");

        manOFFButton.setText("OFF");
        manOFFButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manOFFButtonActionPerformed(evt);
            }
        });

        manChangesButton.setText("Apply Changes");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(manONButton, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(manOFFButton, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(manChangesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(manONButton)
                    .addComponent(manOFFButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(manChangesButton))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Lights", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13))); // NOI18N

        lightsONButton.setText("ON");

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(rightClosedButton, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                .addContainerGap())
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

        jTextField1.setText("72");

        jLabel13.setText("Temp:");

        jLabel14.setText("°");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
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
                            .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void authFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_authFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_authFieldActionPerformed

    private void manOFFButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manOFFButtonActionPerformed
        // TODO add your handling code here:
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

    private void setPowerFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setPowerFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_setPowerFieldActionPerformed

    private void applyBrakeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyBrakeButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_applyBrakeButtonActionPerformed

    private void currentPowerFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentPowerFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_currentPowerFieldActionPerformed

    private void getPowerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getPowerButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_getPowerButtonActionPerformed

    private void lightsOFFButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lightsOFFButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lightsOFFButtonActionPerformed

    private void rightClosedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightClosedButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rightClosedButtonActionPerformed

    private void leftClosedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftClosedButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_leftClosedButtonActionPerformed

    private void applyBrakeButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyBrakeButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_applyBrakeButton1ActionPerformed

    private void applyBrakeButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyBrakeButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_applyBrakeButton2ActionPerformed

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
    private javax.swing.JComboBox<String> TrainList;
    private javax.swing.JToggleButton applyBrakeButton;
    private javax.swing.JToggleButton applyBrakeButton1;
    private javax.swing.JToggleButton applyBrakeButton2;
    private javax.swing.JTextField authField;
    private javax.swing.JLabel authMiles;
    private javax.swing.JTextField calcPowerField;
    private javax.swing.JTextField currentPowerField;
    private javax.swing.JTextField currentSpeedField;
    private javax.swing.JButton getPowerButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
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
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToggleButton leftClosedButton;
    private javax.swing.JToggleButton leftOpenButton;
    private javax.swing.JToggleButton lightsOFFButton;
    private javax.swing.JToggleButton lightsONButton;
    private javax.swing.JButton manChangesButton;
    private javax.swing.JToggleButton manOFFButton;
    private javax.swing.JToggleButton manONButton;
    private javax.swing.JTextField maxSafeField;
    private javax.swing.JToggleButton rightClosedButton;
    private javax.swing.JToggleButton rightOpenButton;
    private javax.swing.JTextField safeBrakeField;
    private javax.swing.JButton setMaxSafeButton;
    private javax.swing.JTextField setPowerField;
    private javax.swing.JTextField setSpeedField;
    private javax.swing.JButton setSuggestedButton;
    private javax.swing.JTextField suggestedField;
    private javax.swing.JLabel upcomingStationLabel;
    // End of variables declaration//GEN-END:variables
}
