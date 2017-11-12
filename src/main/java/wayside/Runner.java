package wayside;

public class Runner {
    public static void main(String[] args) {
        WaysideController.init();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                WaysideController.openWindow();
            }
        });
    }
}