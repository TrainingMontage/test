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
    double power;
    double temperature;
    double velocity;
    double maxPower;
    double mass;
    double t;
    NumberFormat formatter = new DecimalFormat("#0.00");
    
    
    public Train(){
        power = 0;
        mass = 37103.86; //empty train
    }
    public double getPower(){
        return power;
    }
    public double getVelocity(){
        return velocity;
    }
    public void setTime(double timeInput){
        t = timeInput;
    }
    public void setPower(double powerInput){
        power = powerInput;
        if(power >= 0){
                velocity += Math.sqrt((power*2*t)/mass);
                convertVelocity(velocity);

        }
        
    }

    private void convertVelocity(double velocity1) {
        velocity = velocity * 2.2369;
    }
}
