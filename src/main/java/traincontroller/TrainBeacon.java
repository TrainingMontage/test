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
import trackmodel.StaticBlock;
import trackmodel.StaticSwitch;
import trackmodel.StaticTrack;
//import java.util.IllegalFormatException;
/**
 *
 * @author Didge
 */
public class TrainBeacon {
    int contents;
    
    /*
    A train beacon has 32 bits.
    It will be different depending on whether it's for a station or for a switch.
    
    First bit will be 0/1: 0 for station, 1 for switch
    
    STATION:
    The next two bits are the doors; 0 for both closed (should never happen), to 3 for both open
    The rest of the byte is blank.
    The next byte is the block ID (so we know what station it is)
    the next 2 bytes are the distance to the station from the beacon
    
    SWITCH:
    One byte for "north" branch
    One byte for "south" branch
    One byte empty
    */
    
    boolean type; // false for station, true for switch.
    int northID;
    int southID;
    int switchID;
    
    int stationID;
    int stationDist;
    int doors;
    
    public TrainBeacon(int beacon) throws BadBeaconException {
        contents = beacon;
        // Extract type
        // It's a station
        if(((contents&0x80000000)>>31) == 0)
        {
            type = false;
            doors = (contents&0x60000000)>>29;
            stationID = (contents&0x00FF0000)>>16;
            stationDist = contents&0x0000FFFF;
        }
        // It's a switch
        else if(((contents&0x80000000)>>31) == 1)
        {
            type = true;
            northID = (contents&0x00FF0000)>>16;
            southID = (contents&0x0000FF00)>>8;
            switchID = (contents&0x000000FF);
        }
        else
            throw new BadBeaconException();
        
    }
    
    public boolean getType() {
        return type;
    }
    
    public int getNorthID() {
        if(type)
            return northID;
        else
            return -1;
    }
    
    public int getSouthID() {
        if(type)
            return southID;
        else
            return -1;
    }
    
    public int getSwitchID() {
        if(type)
            return switchID;
        else
            return -1;
    }
    
    public int getStationID() {
        if(!type)
            return stationID;
        else
            return -1;
    }
    
    public int getStationDist() {
        if(!type)
            return stationDist;
        else
            return -1;
    }
    
    public int getDoors() {
        if(!type)
            return doors;
        else
            return -1;
    }
    
    public static TrainBeacon createStationBeacon(int doors, int stationID, int stationDist) throws BadBeaconException {
        int beacon = 0;
        if((doors > 3 || doors < 0) || (stationID > 255 || stationID < 0) || (stationDist > 65535 || stationDist < 0))
        {
            System.err.println("Bad Formatting");
            return null;
        }
        else
        {
            beacon = doors<<29;
            beacon += stationID<<16;
            beacon += stationDist;
        }
        return new TrainBeacon(beacon);
    }
    
    public static TrainBeacon createSwitchBeacon(int North, int South, int ID) throws BadBeaconException {
        int beacon = 0;
        if((South > 255 || South < 0) || (North > 255 || North < 0) || (ID > 255 || ID < 0))
        {
            System.err.println("Bad Formatting");
            return null;
        }
        else
        {
            beacon = North<<16;
            beacon += South<<8;
            beacon += ID;
        }
        return new TrainBeacon(beacon);
    }
}
