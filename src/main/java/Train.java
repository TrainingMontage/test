/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// package traincontroller;
import java.util.Random;
import java.lang.Math;

/**
 *
 * @author Didge
 */
public class Train {
    
    double power;
    double velocity;
    double suggested;
    double authority;
    double maxPower; // watts
    double mass; // kilograms
    double t;
    double brake;
    double brakePwr;
    
    public Train(){
        power = 0;
        velocity = new Random(System.nanoTime()).nextDouble()*20+10;
        suggested = new Random(System.nanoTime()).nextDouble()*20+30;
        authority = new Random(System.nanoTime()).nextDouble()*2000+300;
//        maxPower = 120000; // watts
//        maxPower = 4637.98; // watts, for maximum acceleration
        mass = 37103.86; // kilograms
        maxPower = (0.5*0.5*mass)/2;
        t = 1; // time in seconds between updates (ie, period)
        brake = 1.2; // m/s^2
//        brakePwr = 26714.8; // watts, negative
        brakePwr = (brake*brake*mass)/2;
    }
    
    public Train(double initSpeed){
        power = 0;
        velocity = initSpeed;
        suggested = new Random(System.nanoTime()).nextDouble()*20+10;
        authority = new Random(System.nanoTime()).nextDouble()*2000+300;
//        maxPower = 120000; // watts
//        maxPower = 4637.98; // watts, for maximum acceleration
        mass = 37103.86; // kilograms
        maxPower = (0.5*0.5*mass)/2;
        t = 1; // time in seconds between updates (ie, period)
        brake = 1.2; // m/s^2
//        brakePwr = 26714.8; // watts, negative
        brakePwr = (brake*brake*mass)/2;
    }
    
    public double getVelocity(){
        return velocity;
    }

    public void setPower(double newPower){
        power = newPower;
//        System.err.println("TRAIN setting power.");
        if(power >= 0)
            velocity += Math.sqrt((power*2*t)/mass);
        else
            velocity -= Math.sqrt((power*2*t*-1)/mass);
        if(velocity < 0)
            velocity = 0;
//        System.err.println("TRAIN New Velocity: " + velocity);
    }
    
    public double getSuggestedSpeed(){
        return suggested;
    }
    
    public double getAuthority(){
        return authority;
    }
    
    public double getMaxPower(){
        return maxPower;
    }
    
    public double getBrake(){
        return brake;
    }
    
    public double getBrakePower(){
        return brakePwr;
    }
    
    public void reset(){
        power = 0;
        velocity = new Random(System.nanoTime()).nextDouble()*20+10;
        suggested = new Random(System.nanoTime()).nextDouble()*20+30;
        authority = new Random(System.nanoTime()).nextDouble()*2000+300;
    }
    
    public void reverse(){
        power = 0;
        velocity = new Random(System.nanoTime()).nextDouble()*20+30;
        suggested = new Random(System.nanoTime()).nextDouble()*20+10;
        authority = new Random(System.nanoTime()).nextDouble()*2000+300;
    }
    
    public void setSuggested(double newSuggestion) {
        suggested = newSuggestion;
    }
    
    public void randSuggested() {
        suggested = new Random(System.nanoTime()).nextDouble()*20+30;
        authority = new Random(System.nanoTime()).nextDouble()*2000+300;
    }
}
