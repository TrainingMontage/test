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

import shared.Suggestion;
import wayside.trackmock.WCTrackModel;
import wayside.WaysideController;
import wayside.WaysideControllerGUI;


public class UILayer {

    private static WaysideControllerGUI gui;

    public static void init() {
        gui = new WaysideControllerGUI();
        gui.setVisible(true);
    }

    public static void update() {
        // occupancy();
        // switches();
        // signals();
        // crossings();
        // actualSpeed();
        // actualAuthority();
    }

    public static void submitAction() {
    }
}
/*
        WCTrackModel.init();
        System.out.println("OCCUPANCY: ");
        System.out.println(gui.getOccupancy());
        for (String b: gui.getOccupancy().split(",")) {
            try {
                int block = Integer.parseInt(b.trim());
                WCTrackModel.setOccupancy(block, true);
            } catch (NumberFormatException ex) {
                System.out.println("Please input good numbers in Occupancy tab");
            }
        }
        System.out.println("SWITCHES: ");
        System.out.println(gui.getSwitch());
        for (String b: gui.getSwitch().split(",")) {
            try {
                int block = Integer.parseInt(b.trim());
                WCTrackModel.setSwitch(block, true);
            } catch (NumberFormatException ex) {
                System.out.println("Please input good numbers in Switches tab");
            }
        }
        System.out.println("SIGNALS: ");
        System.out.println(gui.getSignal());
        for (String b: gui.getSignal().split(",")) {
            try {
                int block = Integer.parseInt(b.trim());
                WCTrackModel.setSignal(block, true);
            } catch (NumberFormatException ex) {
                System.out.println("Please input good numbers in Lights tab");
            }
        }
        System.out.println("CROSSINGS: ");
        System.out.println(gui.getCrossing());
        for (String b: gui.getCrossing().split(",")) {
            try {
                int block = Integer.parseInt(b.trim());
                WCTrackModel.setCrossing(block, true);
            } catch (NumberFormatException ex) {
                System.out.println("Please input good numbers in Crossings tab");
            }
        }
        System.out.println("SUGG SPEED: ");
        System.out.println(gui.getSuggestedSpeed());
        System.out.println("SUGG AUTH: ");
        System.out.println(gui.getSuggestedAuthority());
        WaysideController.suggest(craftSuggestion(
            gui.getSuggestedSpeed(),
            gui.getSuggestedAuthority()));
        update();
    }

    private static Suggestion[] craftSuggestion(String speed, String auth) {
        int iSpeed = Integer.parseInt(speed.trim());
        String[] strAuth = auth.trim().split(",");
        int[] authList = new int[strAuth.length];
        for (int i = 0; i < strAuth.length; i++) {
            authList[i] = Integer.parseInt(strAuth[i].trim());
        }
        return new Suggestion[] {new Suggestion(authList[0], iSpeed, authList)};
    }

    private static void occupancy() {
        if (gui == null) return; // Running headless.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < WCTrackModel.TRACK_LEN; i++) {
            sb.append(i);
            sb.append(": ");
            sb.append(WaysideController.isOccupied(i));
            sb.append("\n");
        }
        gui.setOccupancy(sb.toString());
    }

    private static void switches() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < WCTrackModel.TRACK_LEN; i++) {
            if (WCTrackModel.SWITCH == i) {
                sb.append(i);
                sb.append(": ");
                sb.append(WCTrackModel.getSwitch(i));
                sb.append("\n");
            }
        }
        gui.setSwitch(sb.toString());
    }

    private static void signals() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < WCTrackModel.TRACK_LEN; i++) {
            if (WCTrackModel.SWITCH == i || WCTrackModel.ACTIVE_LEAF == i || WCTrackModel.DEFAULT_LEAF == i) {
                sb.append(i);
                sb.append(": ");
                sb.append(WCTrackModel.getSignal(i));
                sb.append("\n");
            }
        }
        gui.setSignal(sb.toString());
    }

    private static void crossings() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < WCTrackModel.TRACK_LEN; i++) {
            if (WCTrackModel.CROSSING == i) {
                sb.append(i);
                sb.append(": ");
                sb.append(WCTrackModel.getCrossing(i));
                sb.append("\n");
            }
        }
        gui.setCrossing(sb.toString());
    }

    private static void actualSpeed() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < WCTrackModel.TRACK_LEN; i++) {
            sb.append(i);
            sb.append(": ");
            sb.append(WCTrackModel.getTrainSpeed(i));
            sb.append("\n");
        }
        gui.setActualSpeed(sb.toString());
    }

    private static void actualAuthority() {
        if (gui == null) return;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < WCTrackModel.TRACK_LEN; i++) {
            sb.append(i);
            sb.append(": ");
            sb.append(WaysideController.getAuthority(i));
            sb.append("\n");
        }
        gui.setActualAuthority(sb.toString());
    }
}*/