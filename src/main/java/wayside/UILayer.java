
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
        // TODO
    }

    private static void occupancy(boolean[] list) {
        if (gui == null) return; // Running headless.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TrackModel.TRACK_LEN; i++) {
            sb.append(i);
            sb.append(": ");
            sb.append(list[i]);
            sb.append("\n");
        }
        gui.setOccupancy(sb.toString());
    }

    private static void actualAuthority(boolean[] list) {
        if (gui == null) return;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TrackModel.TRACK_LEN; i++) {
            if (list[i]) {
                sb.append(i);
                sb.append(": ");
                sb.append(list[i]);
                sb.append("\n");
            }
        }
        gui.setActualAuthority(sb.toString());
    }
}