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
import java.lang.Exception;
import shared.*;
import trainmodel.Train;
import trackmodel.TrackModel;
import trackmodel.StaticBlock;
import trackmodel.StaticSwitch;
import trackmodel.StaticTrack;
import java.util.IllegalFormatException;
/**
 *
 * @author Didge
 */
public class TrainBeacon {
    int contents;
    
    /*
    A train beacon has 32 bits.
    It is split into two sections
    
    stn? door crnt blk    swch?     crnt blk
    0000 0000 0000 0000 | 0000 0000 0000 0000
    31      24
    2^31                  2^15
    
    station is  0x10000000
    door is     0x0F000000
    sttn ID is  0x00FF0000
    
    switch is   0x00008000
    swch ID is  0x00000FFF
    */
    
    boolean swit, sttn; // false for station, true for switch.
    int swit_ID;
    int sttn_ID;
    int doors;
    
    public TrainBeacon(int beacon) throws BadBeaconException {
        contents = beacon;
        // Extract type
        // It's a station
        if(((contents&0x10000000)>>31) == 1)
        {
            sttn = true;
            doors = (contents&0x0F000000)>>24;
            sttn_ID = (contents&0x00FF0000)>>16;
        }
        // It's a switch
        if(((contents&0x8000)>>15) == 1)
        {
            swit = true;
            swit_ID = (contents&0x00000FFF);
        }
        
    }
    
    public boolean isStation() {
        return sttn;
    }
    
    public boolean isSwitch() {
        return swit;
    }
    
    public int getSwitchID() {
        if(swit)
            return swit_ID;
        else
            return -1;
    }
    
    public int getStationID() {
        if(sttn)
            return sttn_ID;
        else
            return -1;
    }
    
    public int getDoors() {
        if(sttn)
            return doors;
        else
            return 0;
    }
}
