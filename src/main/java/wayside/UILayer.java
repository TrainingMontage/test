
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
        signals();
        crossings();
        actualSpeed();
        actualAuthority();
    }

    public static void submitAction() {
        System.out.println("OCCUPANCY: ");
        System.out.println(gui.getOccupancy());
        System.out.println("SWITCHES: ");
        System.out.println(gui.getSwitch());
        System.out.println("SIGNALS: ");
        System.out.println(gui.getSignal());
        System.out.println("CROSSINGS: ");
        System.out.println(gui.getCrossing());
        System.out.println("SUGG SPEED: ");
        System.out.println(gui.getSuggestedSpeed());
        System.out.println("SUGG AUTH: ");
        System.out.println(gui.getSuggestedAuthority());
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
            if (TrackModel.SWITCH == i) {
                sb.append(i);
                sb.append(": ");
                sb.append(TrackModel.getSwitch(i));
                sb.append("\n");
            }
        }
        gui.setSwitch(sb.toString());
    }

    private static void signals() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TrackModel.TRACK_LEN; i++) {
            if (TrackModel.SWITCH == i || TrackModel.ACTIVE_LEAF == i || TrackModel.DEFAULT_LEAF == i) {
                sb.append(i);
                sb.append(": ");
                sb.append(TrackModel.getSignal(i));
                sb.append("\n");
            }
        }
        gui.setSignal(sb.toString());
    }

    private static void crossings() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TrackModel.TRACK_LEN; i++) {
            if (TrackModel.CROSSING == i) {
                sb.append(i);
                sb.append(": ");
                sb.append(TrackModel.getCrossing(i));
                sb.append("\n");
            }
        }
        gui.setCrossing(sb.toString());
    }

    private static void actualSpeed() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TrackModel.TRACK_LEN; i++) {
            if (TrackModel.getTrainSpeed(i) != 0) {
                sb.append(i);
                sb.append(": ");
                sb.append(TrackModel.getTrainSpeed(i));
                sb.append("\n");
            }
        }
        gui.setActualSpeed(sb.toString());
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