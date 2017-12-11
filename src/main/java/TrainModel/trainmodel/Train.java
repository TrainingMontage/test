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
    protected TrackModel _tm;
    protected int authority;
    protected double power; //W
    protected double maxPower = 120000.0; //Watts
    protected double temperature; //
    protected double velocity;
    protected double maxVelocity = 70.0; //km/h
    protected double mass = 37103.86 * 2; //kg empty train 40.9 tons 2 cars;
    protected int numCrew = 1;
    protected int numCars = 2;
    protected int numPassengers;
    protected int maxPassengers = 444; //222 for one car 444 for two
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

    public Train(int newTrainId, int newblockId, TrackModel _tm){
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
        trainController = mock(TrainController.class);
        when(trainController.getPower()).thenReturn(50.0);

        // register with track
        _tm.initializeTrain(this.trainId, this.blockId);


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
        gui.numCarsDisplayLabel.setText(numCars +"");
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
    public Train(int newTrainId, int newblockId, TrackModel _tm, int test){
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
        trainController = mock(TrainController.class);
        when(trainController.getPower()).thenReturn(50.0);

        // register with track
        _tm.initializeTrain(this.trainId, this.blockId);

    }

    public TrainModelGUI getGUI(){
        return gui;
    }

    /**
     * @return The train id of the object.
     */
    public int getTrainId(){
        return trainId;
    }

    /**
     * Method for the Train Controller to get the current power
     * @return The current power
     */
    public double getPower(){
        return power;
    }

    /**
     * @return current velocity of the train
     */
    public double getCurrentVelocity(){
        return velocity;
    }

    public double getVelocity(){
        double newPower = trainController.getPower();
        setPower(newPower,0);
        return velocity;
    }

    /**
     * Method for the Track Model to set the number of passengers in the train
     * @param  passengers The number of passengers that will be boarding
     * @return Number of passengers
     */
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

    /**
     * @return The number of passengers on the train currently
     */
    public int getPassengers(){
        return numPassengers;
    }
    /*
    GUI Testing
    public void setTime(double timeInput){
        time = timeInput;
    }
    */

    /**
     * Method for the Train Controller to set the display when approaching stops.
     * @param message The message to be displayed
     */
    public void setDisplay(String message){
        //send to GUI
        gui.messageBoardDisplayLabel.setText(message);


    }

    /**
     * @return The max number of passengers allowed on the train.
     */
    public int getMaxPassengers(){
        return maxPassengers;
    }

    /**
     * Method for the Track Model to get the length of the train
     * @return The length of the train
     */
    public double getLength(){
        return length;
    }

    /**
     * Helper method when setting the power
     * @return The max power the train can handle
     */
    public double getMaxPower(){
        return maxPower;
    }

    /**
     * Method for the train controller to see if the train has changed blocks
     * @return true if changed else false
     */
    public boolean blockChange(){
        return TrackModel.getTrackModel().getTrainBlockChange(trainId);
    }

    /**
     * Method for the Train Controller to see the authority
     * @return a boolean from the Track Model
     */
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

    /**
     * Method for the Train Controller to receive the suggested speed
     * @return suggested speed from Track Models
     */
    public double getSuggestedSpeed(){
        if(signalFailure){
            return 0.0;
        }
        else{
            return TrackModel.getTrackModel().getTrainSpeed(trainId);
        }
    }

    /**
     * Method for the Train Controller to get the beacon information
     * @return an integer from the Track Model
     */
    public int getBeacon(){
        return TrackModel.getTrackModel().getTrainBeacon(trainId);
    }

    /**
     * Helper method to get the grade from the Track Model
     * @return the grade from the Track Model
     */
    public double getGrade(){
        grade = TrackModel.getTrackModel().getGrade(trainId);
        return grade;
    }

    /**
     * Method for the Train Controller to set the power for the train
     * @param powerInput the power to be set
     */
    public void setPower(double powerInput, int test){
        if(test == 1){
            when(trainController.getPower()).thenReturn(powerInput);
        }
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
            gui.powerDisplayLabel.setText(String.format("%.2f", getPower()/1000) + "W");
        }
    }

    /**
     * Calculates the sin of an angle
     * @param  gradePercentage the percent of grade of the current block
     * @return the sin of an angle after converting it from a percentage
     */
    public double sine(double gradePercentage){
        double angle = Math.atan(gradePercentage/100);
        return Math.sin(angle);
    }

    /**
     * @return boolean returns the current status of the service brakes
     */
    public boolean getServiceBrakes(){
        return serviceBrake;
    }

    /**
     * Turn service brakes on or off
     * @param status the new status to be set
     */
    public void setServiceBrakes(boolean status){
        if(status == true){
            serviceBrake = true;
        }
        else{
            serviceBrake = false;
        }
    }

    /**
     * @return boolean returns the current status of the emergency brakes
     */
    public boolean getEmergencyBrakes(){
        return emergencyBrake;
    }

    /**
     * Turn emergency brakes on or off
     * @param status the new status to be set
     */
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

    /**
     * Method for the Train Controller to get the emergency brake rates of the train
     * @return The rate of declaration when emergency brakes are applied
     */
    public double getEmergencyBrakeRate(){
        return emergencyBrakeRate;
    }

    /**
     * Method for the Train Controller to get the service brake rates of the train
     * @return The rate of declaration when service brakes are applied
     */
    public double getServiceBrakeRate(){
        return serviceBrakeRate;
    }

    /**
     * Helper method to update the velocity after given a power.
     * @param forceApplied the force after a new power is set.
     */
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

    /**
     *
     * @return The current temperature of the train.
     */
    public double getCurrentTemperature(){
        return temperature;
    }

    /**
     *
     * @param newTemp desired new inside temperature
     */
    public void setTemperature(double newTemp){
        temperature = newTemp;
    }

    /**
     * @return true if on and false if off
     */
    public boolean getLights(){
        return lights;
    }

    /**
     * Change the status of the lights
     * @param  status The new status of the lights
     * @return true if on and false if off
     */
    public boolean setLights(boolean status){
        lights = status;
        if(gui != null){
            if(getLights() == true){
                gui.lightsDisplayLabel.setText("ON");
            }
            else if(getLights() == false){
                gui.lightsDisplayLabel.setText("OFF");
            }
            else{
                gui.lightsDisplayLabel.setText("--");
            }
        }
        return getLights();
    }

    /**
     * @return 0 if closed else 1 if open
     */
    public int getLeftDoor(){
        return leftDoor;
    }

    /**
     * Change the status of the left door
     * @param  newStatus The new status that the left door should be set to
     * @return 0 if close else 1 if open
     */
    public int setLeftDoor(int newStatus){
        //0 is closed
        //1 is open
        if(newStatus == 0 || newStatus == 1){
            leftDoor = newStatus;
        }
        else{
            leftDoor = 0;
        }
        if(gui != null){
            if(getLeftDoor() == 1){
                gui.leftDoorDisplayLabel.setText("OPEN");
            }
            else if(getLeftDoor() == 0){
                gui.leftDoorDisplayLabel.setText("CLOSED");
            }
            else{
                gui.leftDoorDisplayLabel.setText("--");
            }
        }

        return leftDoor;
    }

    /**
     * @return 0 if closed else 1 if open
     */
    public int getRightDoor(){
        return rightDoor;
    }

    /**
     * Change the status of the right door
     * @param  newStatus   The new status that the right door should be set to
     * @return 0 if closed else 1 if open
     */
    public int setRightDoor(int newStatus){
        //0 is closed
        //1 is open
        if(newStatus == 0 || newStatus == 1){
            rightDoor = newStatus;
        }
        else{
            rightDoor = 0;
        }
        if(gui != null){
            if(getRightDoor() == 1){
                gui.rightDoorDisplayLabel.setText("OPEN");
            }
            else if(getRightDoor() == 0){
                gui.rightDoorDisplayLabel.setText("CLOSED");
            }
            else{
                gui.rightDoorDisplayLabel.setText("--");
            }
        }
        return rightDoor;
    }

    /**
     * Helper method to calculate displacement
     * @return The last updated time
     */
    public int getTime(){
        return time;
    }

    /**
     * Turn signal failure on/off
     * @param status new status of the signal failure mode
     */
    public void signalFailureMode(boolean status){
        signalFailure = status;
        setEmergencyBrakes(status);
    }

    /**
     * Turn brake failure on/off
     * @param status new status of the brake failure mode
     */
    public void brakeFailureMode(boolean status){
        brakeFailure = status;
        setEmergencyBrakes(status);
    }

    /**
     * Turn engine failure on/off
     * @param status new status of the engine failure mode
     */
    public void engineFailureMode(boolean status){
        engineFailure = status;
        setEmergencyBrakes(status);
    }

    /**
     * Calculates the distances traveled from the last time it was called
     * @return The distance traveled returned to the Track Model
     */
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
