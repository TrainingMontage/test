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
import java.util.Arrays;
import java.sql.SQLException;
import shared.*;
import trainmodel.Train;
import trackmodel.TrackModel;
import trackmodel.StaticBlock;
import trackmodel.StaticSwitch;
import trackmodel.StaticTrack;

/**
 *
 * @author Didge
 */
public class TrainController implements TrainControllerInterface {

    static TrainControllerUI UI;
    static boolean UIexists = false;
    static int numTrains = 0;
    static TrainController[] allTrainControllers = new TrainController[100];
//    static StaticTrack theTrack;
    static protected MapTracker theMap;
    
    protected Train theTrain;

    protected boolean onSwitch = false;
    protected double t; // delta time, in seconds
    private double lastTime;
    // ultimate gain
    private double ku;
    private boolean kikpset = false;
    private double setKI;
    private double setKP;
    
    // Station Beacon Data
    private boolean stationUpcoming;
    private int distToStation;
    private int stationID;
    private int doorstatus;
    private int stopTime = -1;
    
    // Set initial last values to 0
    private double lastE;
    private double lastU;
    // Set initial power to 0
    protected double Pcmd;
    protected double distanceTraveled = 0; // Distance traveled along current block
    
    // Other parameters
    protected boolean leftShouldBeOpen = false;
    protected boolean rightShouldBeOpen = false;
    protected boolean lightsShouldBeOn = true;
    protected boolean applyBrakes = false;
    protected boolean applyEBrakes = false;
    protected boolean stop = false;
    protected boolean estop = false;
    protected boolean coast = false;
    protected String station = "";
    private int ID;
//    private int index;
    private boolean t_override = false;
    
    public static void initUI() {
        TrainControllerUI TCUI = new TrainControllerUI();
        TCUI.initialize();
    }
    
    /**
     * Main does nothing.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {        
        
    }
    
    public TrainController() {
//        theTrain = new Train();
        theTrain = null;
        lastTime = Environment.clock;// seconds
        // ultimate gain
        ku = 0;
    
        // Set initial last values to 0
        lastE = 0;
        lastU = 0;
        // Set initial power to 0
        Pcmd = 0;
        ID = 0;
        addTrainController(this);
        theMap = new MapTracker(theTrain);
        UI = new TrainControllerUI();
        UI.initialize(this);
        UIexists = true;
    }
    
    public TrainController(int trainID) {
//        theTrain = new Train();
        theTrain = null;
        lastTime = Environment.clock;// seconds
        // ultimate gain
        ku = 0;
    
        // Set initial last values to 0
        lastE = 0;
        lastU = 0;
        // Set initial power to 0
        Pcmd = 0;
        ID = trainID;
        addTrainController(this);
        theMap = new MapTracker(theTrain);
        
        UI = new TrainControllerUI();
        UI.initialize(this);
        UIexists = true;
    }
    
    public TrainController(Train newTrain, int trainID) {
        theTrain = newTrain;
        lastTime = Environment.clock;// seconds
        // ultimate gain
        ku = 0;
    
        // Set initial last values to 0
        lastE = 0;
        lastU = 0;
        // Set initial power to 0
        Pcmd = 0;
        ID = trainID;
        addTrainController(this);
        theMap = new MapTracker(theTrain);
//        theTrack = TrackModel.getTrackModel().getStaticTrack();
//        try {
//            theTrack = TrackModel.getTrackModel().getStaticTrack();
//        } catch(SQLException | ClassNotFoundException e) {
//            System.err.println("You done effed up.");
//        }
        UI = new TrainControllerUI();
        UI.initialize(this);
        UIexists = true;
    }
    
    public TrainController(boolean showUI, Train newTrain, int trainID) {
        theTrain = newTrain;
        lastTime = Environment.clock;// seconds
        // ultimate gain
        ku = 0;

        // Set initial last values to 0
        lastE = 0;
        lastU = 0;
        // Set initial power to 0
        Pcmd = 0;
        ID = trainID;
        addTrainController(this);
        theMap = new MapTracker(theTrain);
        if(showUI)
        {
            UI = new TrainControllerUI();
            UI.initialize(this);
            UIexists = true;
        }
        else
            UIexists = false;
    }
    
    public TrainController(Train newTrain, int trainID, StaticTrack track, StaticBlock startBlock) {
        theTrain = newTrain;
        lastTime = Environment.clock;// seconds
        // ultimate gain
        ku = 0;
    
        // Set initial last values to 0
        lastE = 0;
        lastU = 0;
        // Set initial power to 0
        Pcmd = 0;
        ID = trainID;
        addTrainController(this);
        theMap = new MapTracker(track, startBlock, theTrain);
        UI = new TrainControllerUI();
        UI.initialize(this);
        UIexists = true;
    }
    
    protected void setTrack(StaticTrack newTrack) {
        theMap.setTrack(newTrack);
    }
    
    protected void doAllUpdates() {
        // Update Time
        updateTime();
        // Update map
        if(theMap.blockChange())
        {
            distanceTraveled = 0;
            // Check for a beacon (overrides next block if it signifies a switch)
            if(!checkBeacon()) // If we aren't on a switch, update next block normally
            {
                theMap.getNextBlock();
            }
        }
        
        // If a station is upcoming, update that information
        updateStation();
        
        // Update our distance along the block
        updateDistTraveled();
        if(UIexists)
            UI.updateAll();
    }
    
    /*
    protected void updateNextBlock() {
        if(!onSwitch)
        {
            if(startblock)
            {
                nextBlock = theTrack.getStaticBlock(currentBlock.getNextId());
            }
            else if (currentBlock.getNextId() != lastBlock.getId())
            {
                nextBlock = theTrack.getStaticBlock(currentBlock.getNextId());
            }
            else if (currentBlock.getNextId() != lastBlock.getId())
            {
                nextBlock = theTrack.getStaticBlock(currentBlock.getPreviousId());
            }
        }
    }
    */
    
//    /**
//     * Computes the remaining distance the train can go before reaching the
//     * end of its authority.
//     * 
//     * @return the distance to the end of authority
//     */
    /*
    protected double distToAuthEnd() { 
        // I need the length of the block I'm on, my estimated distance traveled on
        // that block, whether or not I have authority for the next block, and the
        // distance of that block.
        double distToStop = currentBlock.getLength();
        // Add the distance of the next block if we have authority
        if(theTrain.getAuthority())
            distToStop += theTrack.getStaticBlock(currentBlock.getNextId()).getLength();
//        updateDistTraveled();
        distToStop -= distanceTraveled;
        return distToStop;
    }
    */
    
    protected void updateDistTraveled() {
        // get the velocity and the time elapsed
        double vel = theTrain.getCurrentVelocity();
        distanceTraveled += vel * t;
    }
    
    protected void updateTime() {
        if(!t_override)
        {
            t = Environment.clock - lastTime;
            lastTime = Environment.clock;
        }
    }
    
    /**
     * Checks to see if we've changed blocks, resets distanceTraveled.
     * 
     * @return returns true if we've changed block, false if not.
     */
    /*
    protected boolean blockChange() {
        if(theTrain.blockChange())
        {
            if(startblock)
                startblock = false;
            if(onSwitch)
                onSwitch = false;
            distanceTraveled = 0;
            currentBlock = nextBlock;
            return true;
        }
        return false;
    }
    */
    
    protected boolean checkBeacon() {
        TrainBeacon beacon;
        try{
            int beaconID = theTrain.getBeacon();
            if(beaconID == -1) // no beacon
                return false;
            else
            if(beaconID != -1)
            {
               beacon = new TrainBeacon(beaconID);
               if(beacon.getType())
               {
                   // It's a switch, handle appropriately
                   theMap.doSwitchBlock(beacon.getSwitchID());
                   onSwitch = true;
               }
               else if(!beacon.getType())
               {
                   // It's a station, handle appropriately
                   stationUpcoming = true;
                   distToStation = beacon.getStationDist();
                   stationID = beacon.getStationID();
                   doorstatus = beacon.getDoors();
                   station = theMap.getStation(stationID);
                   theMap.getNextBlock(); // Since this checked on block change, we update block.
               }
               return true;
            }
        } catch (BadBeaconException e) {
            System.err.println("Poorly formatted beacon; returning false");
        }
        return false;
    }
    
    protected void updateStation() {
        if(stationUpcoming)
        {
            // Train speed is not zero and we haven't already told it to stop.
            if(theTrain.getCurrentVelocity() != 0 && !stop)
            {
                justStop();
                displayStation("Approaching Station " + station);
            }
            // We've made a non-emergency stop (i.e. at a station)
            else if(theTrain.getCurrentVelocity() == 0 && !estop)
            {
                // Stuff we only do once
                if(stopTime == -1)
                {
                    stopTime = Environment.clock;
                    displayStation(station);
                    setDoors(doorstatus);
                }
                else if(Environment.clock - stopTime == 30) // stop for 30 seconds
                {
                    // Reset everything
                    station = "";
                    displayStation(station);
                    stopTime = -1;
                    youCanGoNow();
                    stationUpcoming = false;
                    doorstatus = 0;
                    setDoors(doorstatus);
                }
            }
        }
    }
    
    
    /**
     * Computes the minimum safe braking distance for a train, given its
     * Speed.
     * 
     * @return is the minimum safe braking distance for the train in question,
     * or -1 if the train is null;
     * 
     * NOTE: Could also simply return void and update the train.
     */
    protected double computeSafeBrake(){
        if(!checkForTrain())
            return -1;
        double distance = 2*theTrain.getCurrentVelocity()*theTrain.getCurrentVelocity();
//        distance /= theTrain.getServiceBrakes();
        distance /= Math.abs(theTrain.getServiceBrakeRate());
        System.err.println("SAFEBRAKE: dist " + distance);
        // d = vt+at^2
        // if a is neg, and we come to a stop,
        // 0 = v-at
        // t = v/a
        // d = v^2/a + v^2/a = 2v^2/a
        return distance;
    }
    
     /**
     * Computes the maximum safe speed for a train, given an authority.
     * 
     * @return is the maximum safe speed for the train in question,
     * or -1 if the train is null;
     * 
     * NOTE: Could also simply return void and update the train.
     */
    protected double computeSafeSpeed(){
        if(!checkForTrain())
            return -1;
        double brakeDist = computeSafeBrake();
        double velocity = 0;
        double distLeft;
        if(stationUpcoming)
            distLeft = distToStation;
        else
            distLeft = theMap.distToAuthEnd(distanceTraveled);
        if(brakeDist > distLeft)
        {
            // Stop immediately using ebrake
            emergencyStop();
        }
        else
//            velocity = Math.sqrt(2*distLeft*theTrain.getServiceBrakes());
            velocity = Math.sqrt(Math.abs(2*distLeft*theTrain.getServiceBrakeRate()));
        System.err.println("SAFESPEED; Brakedist: " + computeSafeBrake());
        System.err.println("SAFESPEED; Velocity: " + velocity);
        if(velocity > theTrain.getSuggestedSpeed())
            velocity = theTrain.getSuggestedSpeed();
        return velocity;
    }
    
    /**
     * Computes the necessary power at which to set a train, given an authority.
     * 
     * @return is the power to which the train should be set.  If negative,
     * represents number of seconds brake should be applied.
     * 
     * NOTE: Could also simply return void and update the train.
     */
     @Override
     public double getPower(){
        doAllUpdates();
        if(estop)
        {
            applyEBrakes = true;
            theTrain.setEmergencyBrakes(true);
//            Pcmd = 0 - theTrain.getEBrakePower();
            return 0;
        }
        if(stop)
        {
            theTrain.setServiceBrakes(true);
            applyBrakes = true;
//            Pcmd = 0 - theTrain.getBrakePower();
            return 0;
        }
        applyBrakes = false;
        applyEBrakes = false;
        double u = 0;
        double Ki, Kp;
        
        // newVel is the Setpoint
        double SP = theTrain.getSuggestedSpeed();
        double safe = computeSafeSpeed();
        // theTrain's velocity is the Process Variable
        double PV = theTrain.getCurrentVelocity();
        
        System.err.println("Safe speed: " + safe);
        
        if(SP >= safe) {
            System.err.println("Setting to safer speed:\t" + safe);
            SP = safe;
        }
        else
            System.err.println("Setting to suggested speed:\t" + SP);
        if(kikpset)
        {
            Ki = setKI;
            Kp = setKP;
        }
        else
        {
            Kp = ku*0.45;
            Ki = ku*1.2/t;
        }
        
        // Truncate speed
        SP = SP * 0.95;
        
        // Compute velocity error
        double e = SP - PV;
        Pcmd = Kp*e + Ki*u;
        
        // Get maximum train power
        double Pmax = theTrain.getMaxPower();
        
        
//        System.err.println("Init Pcmd:\t"+Pcmd);
        if(Pcmd < Pmax){
            u = lastU + (t/2)*(e-lastE);
        }
        else if(Pcmd >= Pmax){
            u = lastU;
        }
        else
            System.err.println("Error in getPower()");
        
//        Pcmd = Kp*e + Ki*u;
        if(Pcmd < 0) // System is slowing down
//            if(Pcmd <= theTrain.getBrakePower()*(-1))
//            if(e < (0 - theTrain.getBrake()))
            if(Pcmd < -1)
            {
                applyBrakes = true;
//                Pcmd = 0 - theTrain.getBrakePower();
                Pcmd = 0;
                theTrain.setServiceBrakes(true);
            }
            else
            {
                coast = true;
//                Pcmd = -1;
                Pcmd = 0;
            }
        else if(Pcmd > Pmax)
        {
            Pcmd = Pmax;
            theTrain.setServiceBrakes(false);
        }
        else
        {
            coast = false;
            theTrain.setEmergencyBrakes(true);
        }
        System.err.println("Final Power:\t" + Pcmd);
        System.err.println("Final u:\t" + u);
        System.err.println("Final e:\t" + e);
        lastU = u;
        lastE = e;
        return Pcmd;
    }
    
    protected void setKu(double newVal) {
        ku = newVal;
        kikpset = false;
    }
    
    protected void setKiKp(double newKi, double newKp) {
        setKI = newKi;
        setKP = newKp;
        kikpset = true;
    }
    
    /**
     * Commands the Train in question to turn their lights on or off.
     * 
     * @param on is true if the lights should be on, and false if the lights
     * should be off.
     * @return true if the lights are on, false if not.
     */
    protected boolean setLights(boolean on){
        lightsShouldBeOn = on;
//        UI.updateLights(this);
        return lightsShouldBeOn;
    }
    
    /**
     * Commands the Train in question to open their right or left doors.
     * 
     * @param leftOpen is true if the left doors should be opened (or remain
     * open); false if the doors should be closed (or remain closed).
     * @param rightOpen is true if the right doors should be opened (or remain
     * open); false if the doors should be closed (or remain closed).
     * @return status; 0 if both are closed; 1 if only right is open; 2 if only
     * left is open; 3 if both are open.
     */
    protected byte setDoors(boolean leftOpen, boolean rightOpen){
        leftShouldBeOpen = leftOpen;
        rightShouldBeOpen = rightOpen;
//        UI.updateDoors(this);
        byte status = 0;
        if(rightShouldBeOpen && !leftShouldBeOpen)
            status = 1;
        else if(!rightShouldBeOpen && leftShouldBeOpen)
            status = 2;
        else if(rightShouldBeOpen && leftShouldBeOpen)
            status = 3;
        return status;
    }
    
    protected byte setDoors(int status) {
        // if neg for some reason, just close both.
        if(status <= 0)
            return setDoors(false, false);
        else if(status == 1)
            return setDoors(false, true);
        else if(status == 2)
            return setDoors(true, false);
        // if for some reason greater than 3, just open both.
        else if(status >= 3)
            return setDoors(true, true);
        // This should never, ever happen.
        return -1;
    }
    
    /**
     * Bring the train to a complete stop regardless of circumstances as quickly
     * as safely possible.
     * 
     * @return status of variable stop
     */
    protected boolean justStop(){
        stop = true;
        return stop;
    }
    
    /**
     * Bring the train to a complete stop regardless of circumstances using the ebrake.
     * 
     * @return status of variable stop
     */
    protected boolean emergencyStop(){
        estop = true;
        return estop;
    }
    
    /**
     * Allow train to start up again.
     * 
     * @return value of stop
     */
    protected boolean youCanGoNow(){
        stop = false;
        estop = false;
        return stop;
    }
    
    /**
     * Does the necessary work to send a train in for maintenance.
     * 
     * @return true if the train is successfully sent for service; false if not.
     */
    protected boolean sendForService(){
        
        return true;
    }
    
    /**
     * Displays Station and the distance to that station.
     * 
     * @param name is the name of the station
     */
    protected void displayStation(String name){
        station = name;
        theTrain.setDisplay(name);
        if(UIexists)
            UI.updateStation(name);
    }
    
    protected void addTrainController(TrainController newTrainController) {
        allTrainControllers[numTrains++] = newTrainController;
        // Double array size if necessary
        if(numTrains >= allTrainControllers.length)
            allTrainControllers = Arrays.copyOf(allTrainControllers, allTrainControllers.length * 2);
    }
    
    protected int getNumTrains() {
        return numTrains;
    }
    
    protected TrainController getTrainControllerByID(int traincontrollerID) {
        for(int i = 0; i < numTrains; i++) {
            if(allTrainControllers[i].getID() == traincontrollerID)
            {
                return allTrainControllers[i];
            }
        }
        return null;
    }
    
    protected int getID() {
        return ID;
    }
    
    protected double setTemp(double temperature) {
        theTrain.setTemperature(temperature);
        return theTrain.getCurrentTemperature();
    }
    
    protected boolean addTrain(Train newTrain, int trainID) {
        if(theTrain != null)
        {
            System.err.println("Train already populated");
            return false;
        }
        else
        {
            theTrain = newTrain;
            ID = trainID;
            return true;
        }
    }
    
    protected boolean checkForTrain() {
        if(theTrain == null)
        {
            System.err.println("No train!");
            return false;
        }
        else
        {
//            System.err.println("There's a train");
            return true;
        }
        
    }
    
    protected void setT(double newT) {
        t = newT;
        t_override = true;
    }
}
