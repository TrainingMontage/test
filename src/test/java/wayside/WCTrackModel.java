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

import shared.BlockStatus;
import trackmodel.TrackModelInterface;
import trackmodel.StaticBlock;
import trackmodel.StaticSwitch;

/**
 * A simple static track model aganist which to test WaysideController.
 */
public class WCTrackModel implements TrackModelInterface {
    
    private boolean[] authority;
    private boolean[] occupied;
    private double[] speed;

    public WCTrackModel(int trackLen) {
        authority = new boolean[trackLen];
        occupied  = new boolean[trackLen];
        speed     = new double[trackLen];
        clear();
    }

    public void clear() {
        for (int i = 0; i < speed.length; i++) {
            authority[i] = false;
            speed[i] = 0;
        }
    }

    public boolean occupy(int blockId, boolean value) {
        occupied[blockId] = value;
        return value;
    }

    public boolean isOccupied(int blockId) {
        return occupied[blockId];
    }

    public boolean setAuthority(int blockId, boolean authority) {
        this.authority[blockId] = authority;
        return authority;
    }

    public boolean getTrainAuthority(int trainId) {
        return authority[trainId];
    }

    public double setSpeed(int blockId, double speed) {
        this.speed[blockId] = speed;
        return speed;
    }

    public double getTrainSpeed(int trainId) {
        return this.speed[trainId];
    }


    /* TRAIN METHODS I DON'T CARE ABOUT */
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

    public StaticSwitch getStaticSwitch(int switchID) {
        return null;
    }

    public StaticBlock getStaticBlock(int blockId) {
        return null;
    }
    public boolean setRepair(int blockId) {
        return false;
    }

    public boolean setOperational(int blockId) {
        return false;
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