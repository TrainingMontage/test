/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traincontroller;
import java.util.Arrays;

/**
 *
 * @author Didge
 */
public class TrainController implements TrainControllerInterface {

//    static TrainControllerUI UI;
    static int numTrains = 0;
    static TrainController[] allTrainControllers = new TrainController[100];
    protected Train theTrain;
    private double t;// seconds
    // ultimate gain
    private double ku;
    private boolean kikpset = false;
    private double setKI;
    private double setKP;
    
    // Set initial last values to 0
    private double lastE;
    private double lastU;
    // Set initial power to 0
    protected double Pcmd;
    
    // Other parameters
    protected boolean leftShouldBeOpen = false;
    protected boolean rightShouldBeOpen = false;
    protected boolean lightsShouldBeOn = true;
    protected boolean applyBrakes = false;
    protected boolean stop = false;
    protected boolean coast = false;
    protected String station = "";
    private int ID;
    private int index;
    
    public static void initUI() {
        TrainControllerUI TCUI = new TrainControllerUI();
        TCUI.initialize();
    }
    
    /**
     * Main simply tests basic functionality.
     * 
     * @param args the command line arguments
     * @throws InterruptedException if the thread.sleep fails.
     */
    public static void main(String[] args) throws InterruptedException {        
        System.err.println("Running Unit Test");
        
        System.err.println("Testing ultimate gains.");
//        double power;
//        Train testTrain = new Train();
//        TrainController TC = new TrainController(testTrain, 1);
//        TrainControllerUI UI = new TrainControllerUI();
//        UI.initialize(TC);
//        UI.updateLights();
//        UI.setVisible(true);
        
        runTest(4);
        System.err.println();
////        for(int j = 1; j < 11; j++) {
////            TC.setKu((double)j);
//            TC.setKu(250);
//            System.err.println("Ku is " + TC.ku);
//            System.err.println("Starting Speed: " + testTrain.getVelocity());
//            System.err.println("Suggested Speed: " + testTrain.getSuggestedSpeed());
//            System.err.println("Authority: " + testTrain.getAuthority());
//            for(int i = 0; i < 75; i++) {
//                power = TC.getPower();
//                System.err.println("Power Set to: " + power + "W");
//                testTrain.setPower(power);
//                System.err.println("Velocity now: " + testTrain.getVelocity());
//                System.err.println();
//            }
//            System.err.println("-------- REVERSE ---------");
//            testTrain.reverse();
//            System.err.println("Starting Speed: " + testTrain.getVelocity());
//            System.err.println("Suggested Speed: " + testTrain.getSuggestedSpeed());
//            System.err.println("Authority: " + testTrain.getAuthority());
//            for(int i = 0; i < 75; i++) {
//                power = TC.getPower();
//                System.err.println("Power Set to: " + power + "W");
//                testTrain.setPower(power);
//                System.err.println("Velocity now: " + testTrain.getVelocity());
//                System.err.println();
//            }
////            System.err.println("-------- END LOOP " + j + " ---------");
//            testTrain.reset();
//        }
        
        
    }
    
    /**
     * Runs a unit test on the system.  The train should start at 0, get to
     * speed, stop at a station, and start the process over, until told to stop.
     * 
     * Will time out after 360 iterations (five minutes)
     * 
     * Minute one: get to speed
     * Minute two: slow to station
     * Thirty seconds: wait at station
     * 
     * @param period is the number of iterations per second
     * @throws InterruptedException if the thread.sleep fails.
     */
    public static void runTest(int period) throws InterruptedException {
        int[] stations = {1,2,3,4};
        int station = 0;
        int timer = 1; // counts
        int iterations = 0;
        boolean testing = true;
        double power;
        Train testTrain = new Train(0);
        TrainController TC = new TrainController(testTrain, 1);
        TrainControllerUI UI = new TrainControllerUI();
        UI.initialize(TC);
//        UI.setVisible(true);
//        testTrain.setSuggested(20);
        TC.setKu(500);
        while(testing) {
            UI.updateAll();
//            UI.updateDoors();
            // Get up to speed
            System.err.println("SPEED:\t"+testTrain.getVelocity());
            power = TC.getPower();
            System.err.println("Power:\t" + power);
            testTrain.setPower(power);
            
            // For when the station is being shown
            if(timer - 30 == 0)
                TC.displayStation("");
            
            // Start braking
            if(timer - 90 == 0)
            {
                testTrain.setSuggested(1);
                TC.displayStation("APPROACHING STATION " + stations[station]);
                System.err.println("APPROACHING STATION " + stations[station]);
                UI.updateStation("APPROACHING STATION " + stations[station]);
            }
            
            // Just stop
            if(timer - 120 == 0)
            {
                TC.displayStation("STATION " + stations[station]);
                System.err.println("STATION " + stations[station]);
                TC.setDoors(true, true);
                TC.justStop();
                station++;
            }
            
            // Wait
            if(timer - 180 == 0)
            {
                TC.displayStation("LEAVING STATION");
                System.err.println("LEAVING STATION");
                TC.setLights(!TC.lightsShouldBeOn);
                TC.setDoors(false, false);
                timer = 0;
                iterations++;
                TC.youCanGoNow();
//                testTrain.setSuggested(20);
                testTrain.randSuggested();
            }
            Thread.sleep(1000/period);
            timer++;
            
            if(iterations >= 3)
                testing = false;
                
        }
        System.err.println("TEST COMPLETE");
        
    }
    
    public TrainController() {
//        theTrain = new Train();
        theTrain = null;
        t = 5;// seconds
        // ultimate gain
        ku = 0;
    
        // Set initial last values to 0
        lastE = 0;
        lastU = 0;
        // Set initial power to 0
        Pcmd = 0;
        ID = 0;
        addTrainController(this);
    }
    
    public TrainController(int trainID) {
        theTrain = new Train();
        t = 5;// seconds
        // ultimate gain
        ku = 0;
    
        // Set initial last values to 0
        lastE = 0;
        lastU = 0;
        // Set initial power to 0
        Pcmd = 0;
        ID = trainID;
        addTrainController(this);
    }
    
    public TrainController(Train newTrain, int trainID) {
        theTrain = newTrain;
        t = 5;// seconds
        // ultimate gain
        ku = 0;
    
        // Set initial last values to 0
        lastE = 0;
        lastU = 0;
        // Set initial power to 0
        Pcmd = 0;
        ID = trainID;
        addTrainController(this);
    }
    
    public TrainController(boolean test, int trainID) {
        if(test)
        {
            theTrain = new Train(true);
        }
        else
        {
            theTrain = new Train();
        }
        t = 5;// seconds
        // ultimate gain
        if(test)
            ku = 500;
        else
            ku = 0;
    
        // Set initial last values to 0
        lastE = 0;
        lastU = 0;
        // Set initial power to 0
        Pcmd = 0;
        ID = trainID;
        addTrainController(this);
    }
    
    /**
     * Computes the remaining distance the train can go before reaching the
     * end of its authority.
     * 
     */
    
    /**
     * Detects that a new block is 
     * 
     */
    
    
    /**
     * Computes the minimum safe braking distance for a train, given its
     * authority.
     * 
     * @return is the minimum safe braking distance for the train in question,
     * or -1 if the train is null;
     * 
     * NOTE: Could also simply return void and update the train.
     */
    protected double computeSafeBrake(){
        if(!checkForTrain())
            return -1;
        double auth = theTrain.getAuthority();
        double distance = 2*theTrain.getVelocity()*theTrain.getVelocity();
        System.err.println("SAFEBRAKE: Auth: " + auth);
        distance /= theTrain.getBrake();
        System.err.println("SAFEBRAKE: dist " + distance);
        // d = vt+at^2
        // if a is neg, and we come to a stop,
        // 0 = v-at
        // t = v/a
        // d = v^2/a + v^2/a = 2v^2/a
        if(distance > auth)
            return distance;
        return auth;
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
        double velocity = Math.sqrt(2*brakeDist*theTrain.getBrake());
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
     public double getPower(){
        if(stop)
        {
            applyBrakes = true;
            Pcmd = 0 - theTrain.getBrakePower();
            return Pcmd;
        }
        applyBrakes = false;
        double u = 0;
        double Ki, Kp;
        
        // newVel is the Setpoint
        double SP = theTrain.getSuggestedSpeed();
        double safe = computeSafeSpeed();
        // theTrain's velocity is the Process Variable
        double PV = theTrain.getVelocity();
        
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
            System.err.println("Error");
        
//        Pcmd = Kp*e + Ki*u;
        if(Pcmd < 0) // System is slowing down
//            if(Pcmd <= theTrain.getBrakePower()*(-1))
//            if(e < (0 - theTrain.getBrake()))
            if(Pcmd < -1)
            {
                applyBrakes = true;
                Pcmd = 0 - theTrain.getBrakePower();
            }
            else
            {
                coast = true;
                Pcmd = -1;
            }
        else if(Pcmd > Pmax)
            Pcmd = Pmax;
        else
            coast = false;
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
     * Allow train to start up again.
     * 
     * @return value of stop
     */
    protected boolean youCanGoNow(){
        stop = false;
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
//        UI.updateStation(station);
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
    
    protected void updateIndex(int index) {
        this.index = index;
    }
    
    protected double setTemp(double temperature) {
        return 0;
    }
    
    protected boolean addTrain(Train newTrain) {
        if(theTrain != null)
        {
            System.err.println("Train already populated");
            return false;
        }
        else
        {
            theTrain = newTrain;
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
}
