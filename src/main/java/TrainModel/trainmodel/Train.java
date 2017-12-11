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
 * @author Parth Dadhania
 */
package trainmodel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.lang.Math;
import trackmodel.TrackModel;
import traincontroller.TrainController;
import shared.Convert;
import shared.Environment;

import static org.mockito.Mockito.*;


public class Train {
    int trainId;
    int blockId;
    protected TrainController trainController;

    protected int authority;
    protected double power; //W
    protected double maxPower = 120000.0; //Watts
    protected double temperature; //
    protected double velocity;
    protected double maxVelocity = 70.0; //km/h
    protected double mass = 37103.86 * 2; //kg empty train 40.9 tons 2 cars;
    protected int numCrew = 1;
    protected int numPassengers;
    protected int maxPassengers = 222;
    protected double passengerWeight = 88.9; //kg average weight of male in America
    protected double totalMass;
    protected double friction = 0.47;
    protected double grade;
    protected int time;
    protected boolean serviceBrake;
    protected double serviceBrakeRate = -1.2; //m/s^2
    protected boolean emergencyBrake;
    protected double emergencyBrakeRate = -2.73; //m/s^2
    protected double gravity = 9.81; //m/s^2
    protected double totalForce;
    protected double acceleration;
    protected double maxAcceleration = 0.5; //m/s^2
    protected boolean lights;
    protected int leftDoor;
    protected int rightDoor;

    //Failures
    protected boolean brakeFailure;
    protected boolean engineFailure;
    protected boolean signalFailure;
    //Standard Train Data
    protected double length = 32.2*2; //meters 2 cars
    protected double height = 3.42; //meters
    protected double width = 2.56;  //meters

    protected TrainModelGUI gui;

    protected NumberFormat formatter = new DecimalFormat("#0.00");


    public Train(int newTrainId, int newblockId) {
        this(newTrainId, newblockId, TrackModel.getTrackModel());
    }

    public Train(int newTrainId, int newblockId, TrackModel tm){
        //trainController = new TrainController(this, trainId);
        trainId = newTrainId;
        blockId = newblockId;
        numPassengers = 0;
        power = 0.0;
        totalMass = mass + (numPassengers*passengerWeight);
        velocity = 0.0;
        acceleration = 0.0;
        grade = 0.0;
        temperature = 72.0; // F
        lights = false;
        leftDoor = 0;
        rightDoor = 0;
        emergencyBrake = false;
        serviceBrake = false;
        time = Environment.clock;

        boolean brakeFailure = false;
        boolean engineFailure = false;
        boolean signalFailure = false;

        //Create controller
        trainController = new TrainController(this, this.blockId);
        // trainController = mock(TrainController.class);
        // when(trainController.getPower()).thenReturn(50.0);

        // register with track
        tm.initializeTrain(this.trainId, this.blockId);


        //GUI
        gui = new TrainModelGUI(this);
        gui.setVisible(true);
        gui.temperatureDisplayLabel.setText(String.format("%.1f", temperature) + "F");
        gui.lengthDisplayLabel.setText(String.format("%.2f",length*3.28084) + "ft");
        gui.widthDisplayLabel.setText(String.format("%.2f", width*3.28084) + "ft");
        gui.heightDisplayLabel.setText(String.format("%.2f", height*3.28084) + "ft");
        gui.massDisplayLabel.setText(String.format("%.2f", mass*2.20462) + "lb");
        gui.crewDisplayLabel.setText(numCrew + "");
        gui.passengersDisplayLabel.setText(numPassengers+"");
        gui.messageBoardDisplayLabel.setText("Nothing To Display Currently");
        if(leftDoor == 0){
            gui.leftDoorDisplayLabel.setText("Closed");
        }
        else{
            gui.leftDoorDisplayLabel.setText("Open");
        }

        if(rightDoor == 0){
            gui.rightDoorDisplayLabel.setText("Closed");
        }
        else{
            gui.rightDoorDisplayLabel.setText("Open");
        }

        if(getLights()){
            gui.lightsDisplayLabel.setText("ON");
        }
        else{
            gui.lightsDisplayLabel.setText("OFF");
        }
        if(getEmergencyBrakes()){
            gui.emergencyBrakeDisplayLabel.setText("ON");
        }
        else{
            gui.emergencyBrakeDisplayLabel.setText("OFF");
        }



    }
    public Train(int newTrainId, int newblockId, TrackModel tm, int test){
        //Test Constructor
        //trainController = new TrainController(this, trainId);
        trainId = newTrainId;
        blockId = newblockId;
        numPassengers = 0;
        power = 0.0;
        totalMass = mass + (numPassengers*passengerWeight);
        velocity = 0.0;
        acceleration = 0.0;
        grade = 0.0;
        temperature = 72.0; // F
        lights = false;
        leftDoor = 0;
        rightDoor = 0;
        emergencyBrake = false;
        serviceBrake = false;
        time = Environment.clock;

        boolean brakeFailure = false;
        boolean engineFailure = false;
        boolean signalFailure = false;

        //Create controller
        // trainController = new TrainController(this, this.blockId);
        trainController = mock(TrainController.class);        //
        when(trainController.getPower()).thenReturn(50.0);

        // register with track
        tm.initializeTrain(this.trainId, this.blockId);

    }

    public int getTrainId(){
        return trainId;
    }
    public double getPower(){
        return power;
    }

    public double getCurrentVelocity(){
        return velocity;
    }

    public double getVelocity(){
        double newPower = trainController.getPower();
        setPower(newPower);
        return velocity;
    }

    public int setPassengers(int passengers){
        if(passengers > maxPassengers){
            numPassengers = maxPassengers;
        }
        else if(passengers < 0){
            numPassengers = 0;
        }
        else{
            numPassengers = passengers;
        }

        if(gui != null){
            gui.passengersDisplayLabel.setText(numPassengers+"");
        }
        return numPassengers;
    }

    public int getPassengers(){
        return numPassengers;
    }
    /*
    GUI Testing
    public void setTime(double timeInput){
        time = timeInput;
    }
    */

    public void setDisplay(String message){
        //send to GUI
        gui.messageBoardDisplayLabel.setText(message);


    }

    public int getMaxPassengers(){
        return maxPassengers;
    }

    public double getLength(){
        return length;
    }
    public double getMaxPower(){
        return maxPower;
    }

    public boolean blockChange(){
        return TrackModel.getTrackModel().getTrainBlockChange(trainId);
    }

    public boolean getAuthority(){
        boolean authority = TrackModel.getTrackModel().getTrainAuthority(trainId);
        if(gui != null){
            gui.authorityDisplayLabel.setText(authority + "");
        }
        if(signalFailure){
            return false;
        }
        else{
            return authority;
        }
    }

    public double getSuggestedSpeed(){
        if(signalFailure){
            return 0.0;
        }
        else{
            return TrackModel.getTrackModel().getTrainSpeed(trainId);
        }
    }

    public int getBeacon(){
        return TrackModel.getTrackModel().getTrainBeacon(trainId);
    }
    public double getGrade(){
        grade = TrackModel.getTrackModel().getGrade(trainId);
        return grade;
    }

    public void setPower(double powerInput){
        double forceApplied;
        double declaration;
        if(velocity == 0){
            forceApplied = powerInput;
        }
        else if(serviceBrake == true || emergencyBrake == true){
            if(emergencyBrake == true){
                declaration = emergencyBrakeRate;
            }
            else{
                declaration = serviceBrakeRate;
            }
            forceApplied = totalMass * declaration;
        }
        else{
            forceApplied = powerInput/velocity;
        }

        if(powerInput < 0){

        }
        else if(powerInput >= maxPower){
            //too much power set to max
            power = maxPower;
            updateSpeed(forceApplied);
        }
        else{
            power = powerInput;
            updateSpeed(forceApplied);
        }
        if(gui != null){
            gui.powerDisplayLabel.setText(String.format("%.2f", getPower()) + "W");
        }
    }

    public double sine(double gradePercentage){
        double angle = Math.atan(gradePercentage/100);
        return Math.sin(angle);
    }

    public boolean getServiceBrakes(){
        return serviceBrake;
    }

    public void setServiceBrakes(boolean status){
        if(status == true){
            serviceBrake = true;
        }
        else{
            serviceBrake = false;
        }
    }

    public boolean getEmergencyBrakes(){
        return emergencyBrake;
    }

    public void setEmergencyBrakes(boolean status){
        if(status == true){
            emergencyBrake = true;
            if(gui != null){
                gui.emergencyBrakeDisplayLabel.setText("ON");
            }

        }
        else{
            emergencyBrake = false;
            if(gui != null){
                gui.emergencyBrakeDisplayLabel.setText("OFF");
            }

        }
    }

    public double getEmergencyBrakeRate(){
        return emergencyBrakeRate;
    }

    public double getServiceBrakeRate(){
        return serviceBrakeRate;
    }


    public void updateSpeed(double forceApplied){
        double nforce = totalMass*gravity*friction*sine(getGrade());
        totalForce = forceApplied - nforce;
        acceleration = totalForce/totalMass;

        if(acceleration >= maxAcceleration){
            acceleration = maxAcceleration;
        }
        if(gui != null){
            gui.accelerationDisplayLabel.setText(String.format("%.2f", acceleration) + "");
        }
        velocity = velocity + acceleration;
        if(velocity < 0){
            velocity = 0.0;
        }
        if(velocity > maxVelocity){
            velocity = maxVelocity;
        }

    }

    public double getCurrentTemperature(){
        return temperature;
    }

    public void setTemperature(double newTemp){
        temperature = newTemp;
    }

    public boolean getLights(){
        return lights;
    }

    public boolean setLights(boolean status){
        lights = status;
        return getLights();
    }

    public int getLeftDoor(){
        return leftDoor;
    }

    public int setLeftDoor(int newStatus){
        //0 is closed
        //1 is open
        if(newStatus == 0 || newStatus == 1){
            leftDoor = newStatus;
        }
        else{
            leftDoor = 0;
        }
        return leftDoor;
    }

    public int getRightDoor(){
        return rightDoor;
    }

    public int setRightDoor(int newStatus){
        //0 is closed
        //1 is open
        if(newStatus == 0 || newStatus == 1){
            rightDoor = newStatus;
        }
        else{
            rightDoor = 0;
        }
        return rightDoor;
    }


    public int getTime(){
        return time;
    }

    public void signalFailureMode(boolean status){
        signalFailure = status;
    }

    public void brakeFailureMode(boolean status){
        emergencyBrake = true;
    }

    public void engineFailureMode(boolean status){
        emergencyBrake = true;
    }

    public double getDisplacement(){
        int lastTime = getTime();
        int currentTime = Environment.clock;
        double dVelocity = getVelocity();
        double displacement;
        displacement = (dVelocity * (currentTime - lastTime) + (.5*(acceleration)*Math.pow((currentTime-lastTime),2)));
        time = Environment.clock;
        if(gui != null){
            gui.currentTrainSpeedDisplayLabel.setText(String.format("%.2f", (getCurrentVelocity() * 2.23694)) + "mph");
            gui.accelerationDisplayLabel.setText(String.format("%.4f", acceleration) + "");
        }
        return displacement;

    }



}
