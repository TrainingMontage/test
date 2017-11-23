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

package wayside.trackmock;

import shared.BlockStatus;
import trackmodel.TrackModelInterface;
import trackmodel.StaticBlock;
import trackmodel.StaticSwitch;
import wayside.trackmock.WCStaticBlock;
import wayside.trackmock.WCStaticSwitch;

import static wayside.WaysideController.TRACK_LEN;

/**
 * A simple static track model aganist which to test WaysideController.
 */
public class WCTrackModel implements TrackModelInterface {
    
    private boolean[] authority;
    private boolean[] occupied;
    private int[] speed;
    private WCStaticSwitch sw;

    public WCTrackModel() {
        int n = TRACK_LEN;
        authority = new boolean[n];
        occupied  = new boolean[n];
        speed     = new int[n];
        switchesAndBlocks();
        clear();
    }

    private void switchesAndBlocks() {
        sw = new WCStaticSwitch(1);
        WCStaticBlock root = new WCStaticBlock(2);
        WCStaticBlock def = new WCStaticBlock(3);
        WCStaticBlock act = new WCStaticBlock(8);
        sw.rootSetter(root);
        sw.defaultLeafSetter(def);
        sw.activeLeafSetter(act);
        root.switchSetter(sw);
        def.switchSetter(sw);
        act.switchSetter(sw);
    }

    public boolean occupy(int blockId, boolean value) {
        occupied[blockId] = value;
        return value;
    }

    public void clear() {
        for (int i = 0; i < speed.length; i++) {
            authority[i] = false;
            speed[i] = 0;
        }
    }

    public boolean getTrainAuthority(int trainId) {
        return authority[trainId];
    }

    public boolean isOccupied(int blockId) {
        return occupied[blockId];
    }

    public BlockStatus getStatus(int blockId) {
        return null;
    }

    public boolean setSignal(int blockId, boolean value) {
        return false;
    }

    public boolean getSignal(int blockId) {
        return false;
    }

    public boolean setSwitch(int blockId, boolean value) {
        return false;
    }

    public boolean getSwitch(int blockId) {
        return false;
    }

    public boolean setCrossingState(int blockId, boolean active) {
        return false;
    }

    public boolean getCrossingState(int blockId) {
        return false;
    }

    public boolean setAuthority(int blockId, boolean authority) {
        return this.authority[blockId];
    }

    public int setSpeed(int blockId, int speed) {
        return this.speed[blockId];
    }

    public StaticSwitch getStaticSwitch(int switchID) {
        if (switchID == 1)
            return sw;
        else
            return null;
    }

    public StaticBlock getStaticBlock(int blockId) {
        return null;
    }

    /* TRAIN METHODS I DON'T CARE ABOUT */
    public double getTrainSpeed(int trainId) {
        return 0;
    }

    public int getTrainBeacon(int trainId) {
        return 0;
    }

    public int getTrainPassengers(int trainId) {
        return 0;
    }

    public boolean isIcyTrack(int trainId) {
        return false;
    }

    public double getGrade(int trainId) {
        return 0;
    }
}