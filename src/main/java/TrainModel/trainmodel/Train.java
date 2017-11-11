/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trainmodel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.lang.Math;

/**
 *
 * @author Parth
 */
public class Train {
    int trainId;
    int authority;
    double power; //W
    double maxPower = 120000.0; //Watts
    double temperature; //
    double velocity;
    double maxVelocity = 70.0; //km/h
    double mass = 37103.86; //kg empty train 40.9 tons;
    int numPassengers;
    int maxPassengers = 222;
    double passengerWeight = 88.9; //kg average weight of male in America
    double totalMass;
    double friction = 0.47;
    double grade;
    double time;
    boolean serviceBrake;
    double serviceBrakeRate = -1.2; //m/s^2
    boolean emergencyBrake;
    double emergencyBrakeRate = -2.73; //m/s^2
    double gravity = 9.8; //m/s^2
    double totalForce;
    double acceleration;
    double maxAcceleration = 0.5; //m/s^2
    boolean lights;
    int leftDoor;
    int rightDoor;

    //Failures
    boolean brakeFailure;
    boolean engineFailure;
    boolean signalFailure;
    //Standard Train Data
    double length = 32.2; //meters
    double height = 3.42; //meters
    double width = 2.56;  //meters

    NumberFormat formatter = new DecimalFormat("#0.00");


    public Train(int newTrainId, int blockId){
        trainId = newTrainId;
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

        boolean brakeFailure = false;
        boolean engineFailure = false;
        boolean signalFailure = false;

    }
    public double getPower(){
        return power;
    }
    public double getVelocity(){
        double mphVelocity = velocity * 2.23693629;
        return velocity;
    }
    public void setTime(double timeInput){
        time = timeInput;
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

    }

    public void updateSpeed(double forceApplied){
        double nforce = totalMass*gravity*friction*sine(grade);
        totalForce = forceApplied - nforce;
        acceleration = totalForce/totalMass;

        if(acceleration >= maxAcceleration){
            acceleration = maxAcceleration;
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
        leftDoor = newStatus;
        return getLeftDoor();
    }

    public int getRightDoor(){
        return rightDoor;
    }

    public int setRightDoor(int newStatus){
        rightDoor = newStatus;
        return getRightDoor();
    }

    public double sine(double gradePercentage){
        double angle = Math.atan(gradePercentage/100);
        return Math.sin(angle);
    }



}