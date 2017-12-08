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
 * @author Isaac Goss
 */

package wayside;


public class WaysideUI extends javax.swing.JFrame {

    
    public WaysideUI() {
        initComponents();
    }

    void setOccupancy(int blockId, boolean value) {
        jTable1.getModel().setValueAt(value, blockId-1, 1);
    }

    void setSwitch(int blockId, boolean value) {
        jTable1.getModel().setValueAt(value, blockId-1, 2);
    }

    void setSignal(int blockId, boolean value) {
        jTable1.getModel().setValueAt(value, blockId-1, 3);
    }

    void setCrossing(int blockId, boolean value) {
        jTable1.getModel().setValueAt(value, blockId-1, 4);
    }

    void setSpeed(int blockId, int value) {
        jTable1.getModel().setValueAt(value, blockId-1, 5);
    }

    void setAuthority(int blockId, boolean value) {
        jTable1.getModel().setValueAt(value, blockId-1, 6);
    }

    
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        plcFileChooser = new javax.swing.JFileChooser();

        jButton4.setText("jButton4");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        final int numBlocks = 152;
        Object[][] data = new Object[numBlocks][7];
        for (int i = 0; i < numBlocks; i++) {
            data[i][0] = i+1;
        }

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            data,
            new String [] {
                "Block ID", "Occupancy", "Switches", "Signals", "Crossing", "Speed", "Authority"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class,
                java.lang.Boolean.class,
                java.lang.Boolean.class,
                java.lang.Boolean.class,
                java.lang.Boolean.class,
                java.lang.Integer.class,
                java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
        }

        jButton1.setText("Open Wayside");

        jButton2.setText("Upload PLC");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plcUploadAction();
            }
        });

        jButton3.setText("Submit");

        plcFileChooser.setDialogTitle("Upload PLC");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        pack();
    }

    private void plcUploadAction() {
        int res = plcFileChooser.showOpenDialog(this);
        if (javax.swing.JFileChooser.APPROVE_OPTION == res) {
            java.io.File file = plcFileChooser.getSelectedFile();
            try {
                WaysideController.setStaticTrack(new WCStaticTrack(file));
            } catch (java.io.IOException ioe) {
                // do nothing...
            } catch (FailedToReadPlc e) {
                System.err.println("Failed to read PLC");
                System.err.println(e.getMessage());
            }
        } else {
            System.out.println("File access cancelled by user");
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WaysideUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JFileChooser plcFileChooser;
    // End of variables declaration//GEN-END:variables
}
