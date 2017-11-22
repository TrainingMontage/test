package trainmodel;

import org.junit.*;
import org.junit.rules.*;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Mock;
import shared.Environment;
import traincontroller.TrainController;


public class TrainModelTest{
    double delta = 0.001;
    Train testTrainObject = new Train(1, 0);

    @Test
    public void testGetTrainId(){
        assertEquals(testTrainObject.getTrainId(), 1);
    }

    @Test
    public void testGetPower(){
        assertEquals(testTrainObject.getPower(), 0.0, delta);
    }

    @Test
    public void testGetCurrentVelocity(){
        assertEquals(testTrainObject.getCurrentVelocity(), 0.0, delta);
    }

    @Test
    public void testGetVelocity(){
        testTrainObject.trainController = mock(TrainController.class);
        when(testTrainObject.trainController.getPower()).thenReturn(10000.0);
        testTrainObject.grade = 0.5;
        testTrainObject.numPassengers = 222;
        testTrainObject.velocity = 10;
        assertEquals(testTrainObject.getVelocity(), testTrainObject.getCurrentVelocity(), delta);
    }

    @Test
    public void testSetPassengers(){
        //Too Many Passengers set
        assertEquals(testTrainObject.setPassengers(500), 222);
        //negative number of passengers
        assertEquals(testTrainObject.setPassengers(-100), 0);
        //base case
        assertEquals(testTrainObject.setPassengers(150), 150);
    }

    @Test
    public void testGetMaxPassengers(){
        assertEquals(testTrainObject.getMaxPassengers(), 222);
    }

    @Test
    public void testGetLength(){
        assertEquals(testTrainObject.getLength(), 32.2, delta);
    }

    @Test
    public void testGetMaxPower(){
        assertEquals(testTrainObject.getMaxPower(), 120000.0, delta);
    }

    @Test
    public void testSetPower(){
        testTrainObject.setPower(100000);
        assertEquals(testTrainObject.getPower(),100000, delta);
    }
    @Test
    public void testSetPowerNegative(){
        testTrainObject.setPower(-100000);
        assertEquals(testTrainObject.getPower(),0, delta);
    }
    @Test
    public void testSetPowerTooLarge(){
        testTrainObject.setPower(300000);
        assertEquals(testTrainObject.getPower(), 120000, delta);
    }

    @Test
    public void testSine(){
        assertEquals(testTrainObject.sine(.5), 0.004999, delta);
    }

    @Test
    public void testGetServiceBrakes(){
        assertEquals(testTrainObject.getServiceBrakes(), false);
    }

    @Test
    public void testSetServiceBrakes(){
        //turn on
        testTrainObject.setServiceBrakes(true);
        assertEquals(testTrainObject.getServiceBrakes(), true);
        //turn off
        testTrainObject.setServiceBrakes(false);
        assertEquals(testTrainObject.getServiceBrakes(), false);
    }

    @Test
    public void testGetEmergancyBrakes(){
        assertEquals(testTrainObject.getEmergancyBrakes(), false);
    }

    @Test
    public void testSetEmergancyBrakes(){
        //turn on
        testTrainObject.setEmergancyBrakes(true);
        assertEquals(testTrainObject.getEmergancyBrakes(), true);
        //turn off
        testTrainObject.setEmergancyBrakes(false);
        assertEquals(testTrainObject.getEmergancyBrakes(), false);
    }

    @Test
    public void testUpdateSpeed(){
        testTrainObject.velocity = 10.0;
        testTrainObject.grade = 0.5;
        testTrainObject.numPassengers = 222;
        testTrainObject.updateSpeed(10000);
        //testTrainObject.updateSpeed(10000) = 10.2454971747 (hand calculation)
        assertEquals(10.2454971747, testTrainObject.getCurrentVelocity(), delta);

    }

    @Test
    public void testGetCurrentTemperature(){
        assertEquals(testTrainObject.getCurrentTemperature(), 72.0, delta);
    }

    @Test
    public void testSetTemperature(){
        testTrainObject.setTemperature(65.0);
        assertEquals(testTrainObject.getCurrentTemperature(), 65.0, delta);
    }

    @Test
    public void testGetLights(){
        assertEquals(testTrainObject.getLights(), false);
    }

    @Test
    public void testSetLights(){
        //turn on
        testTrainObject.setLights(true);
        assertEquals(testTrainObject.getLights(), true);
        //turn off
        testTrainObject.setLights(false);
        assertEquals(testTrainObject.getLights(), false);
    }

    @Test
    public void testGetLeftDoor(){
        assertEquals(testTrainObject.getLeftDoor(), 0);
    }

    @Test
    public void testSetLeftDoor(){
        //closed
        testTrainObject.setLeftDoor(0);
        assertEquals(testTrainObject.getLeftDoor(), 0);
        //open
        testTrainObject.setLeftDoor(1);
        assertEquals(testTrainObject.getLeftDoor(), 1);
        //other
        testTrainObject.setLeftDoor(2);
        assertEquals(testTrainObject.getLeftDoor(), 0);
        testTrainObject.setLeftDoor(-2);
        assertEquals(testTrainObject.getLeftDoor(), 0);
    }

    @Test
    public void testGetRightDoor(){
        assertEquals(testTrainObject.getRightDoor(), 0);
    }

    @Test
    public void testSetRightDoor(){
        //closed
        testTrainObject.setRightDoor(0);
        assertEquals(testTrainObject.getRightDoor(), 0);
        //open
        testTrainObject.setRightDoor(1);
        assertEquals(testTrainObject.getRightDoor(), 1);
        //other
        testTrainObject.setRightDoor(2);
        assertEquals(testTrainObject.getRightDoor(), 0);
        testTrainObject.setRightDoor(-2);
        assertEquals(testTrainObject.getRightDoor(), 0);
    }

    @Test
    public void testGetTime(){
        testTrainObject.time = 5;
        assertEquals(testTrainObject.getTime(), 5);
    }

    @Test
    public void testGetDisplacement(){
        /*Environment.clock = 10;
        testTrainObject.time = 5;
        testTrainObject.acceleration = .5;
        testTrainObject.velocity = 10;
        TrainController controller = mock(TrainController.class);
        when(controller.getPower()).thenReturn(10000.0);
        testTrainObject.grade = 0.5;
        testTrainObject.numPassengers = 222;
        assertEquals(testTrainObject.getDisplacement(), 57.477, delta);
        */
    }




}
