/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trainmodel;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author Parth
 */
public class Train {
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
    double friction = 0.57;
    double time;
    boolean serviceBrake;
    double serviceBrakeRate = -1.2; //m/s^2
    boolean emergencyBrake;
    double emergencyBrakeRate = -2.73; //m/s^2
    double gravity = 9.8; //m/s^2
    double totalForce;
    double acceleration;
    double maxAcceleration = 0.5; //m/s^2

    //Failures
    boolean brakeFailure;
    boolean engineFailure;
    boolean signalFailure;
    //Standard Train Data
    double length = 32.2; //meters
    double height = 3.42; //meters
    double width = 2.56;  //meters

    NumberFormat formatter = new DecimalFormat("#0.00");


    public Train(){
        numPassengers = 0;
        power = 0;
        totalMass = mass + (numPassengers*passengerWeight);
        velocity = 0;
        acceleration = 0;

        boolean brakeFailure = false;
        boolean engineFailure = false;
        boolean signalFailure = false;

    }
    public double getPower(){
        return power;
    }
    public double getVelocity(){
        return velocity;
    }
    public void setTime(double timeInput){
        time = timeInput;
    }
    public void setPower(double powerInput){
        double forceApplied;
        if(velocity == 0){
            forceApplied = powerInput;
        }
        else{
            forceApplied = powerInput/velocity;
        }

        if(powerInput < 0){
            power = 0;
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
        double nforce = totalMass*gravity;
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

}
