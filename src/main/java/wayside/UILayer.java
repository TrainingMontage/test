
package wayside;

import wayside.TrackModel;
import wayside.WaysideController;
import wayside.WaysideControllerGUI;

public class UILayer {

    private static WaysideControllerGUI gui;

    public static void init() {
        gui = new WaysideControllerGUI();
        gui.setVisible(true);
    }

    public static void update() {
        occupancy();
        switches();
        // signals();
        // crossings();
        // actualSpeed();
        actualAuthority();
    }

    private static void occupancy() {
        if (gui == null) return; // Running headless.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TrackModel.TRACK_LEN; i++) {
            sb.append(i);
            sb.append(": ");
            sb.append(WaysideController.isOccupied(i));
            sb.append("\n");
        }
        gui.setOccupancy(sb.toString());
    }

    private static void switches() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TrackModel.TRACK_LEN; i++) {
            if (TrackModel.getSwitch(i)) {
                sb.append(i);
                sb.append(": ");
                sb.append(TrackModel.getSwitch(i));
            }
        }
        gui.setSwitch(sb.toString());
    }

    private static void actualAuthority() {
        if (gui == null) return;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TrackModel.TRACK_LEN; i++) {
            if (WaysideController.getAuthority(i)) {
                sb.append(i);
                sb.append(": ");
                sb.append(WaysideController.getAuthority(i));
                sb.append("\n");
            }
        }
        gui.setActualAuthority(sb.toString());
    }
}