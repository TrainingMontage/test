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

import org.junit.*;
import org.junit.rules.*;
import static org.junit.Assert.*;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.Mock;
import shared.Environment;
import traincontroller.TrainController;
import trackmodel.TrackModel;


public class TrainModelTest{
    double delta = 0.001;
    Train testTrainObject;

    @Before
    public void setup() {
        TrackModel _tm = mock(TrackModel.class);
        testTrainObject = new Train(1, 0, _tm, 1);
    }

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
        TrackModel _tm = mock(TrackModel.class);
        testTrainObject = new Train(1, 0, _tm, 1);
        testTrainObject.trainController = mock(TrainController.class);
        when(testTrainObject.trainController.getPower()).thenReturn(10000.0);
        testTrainObject.numPassengers = 444;
        testTrainObject.velocity = 10;
        Train spyTrain = spy(testTrainObject);
        doReturn(0.5).when(spyTrain).getGrade();
        assertEquals(spyTrain.getVelocity(), spyTrain.getCurrentVelocity(), delta);
    }

    @Test
    public void testSetPassengers(){
        //Too Many Passengers set
        assertEquals(testTrainObject.setPassengers(500), 444);
        //negative number of passengers
        assertEquals(testTrainObject.setPassengers(-100), 0);
        //base case
        assertEquals(testTrainObject.setPassengers(150), 150);
    }

    @Test
    public void testGetMaxPassengers(){
        assertEquals(testTrainObject.getMaxPassengers(), 444);
    }

    @Test
    public void testGetLength(){
        assertEquals(testTrainObject.getLength(), 64.4, delta);
    }

    @Test
    public void testGetMaxPower(){
        assertEquals(testTrainObject.getMaxPower(), 120000.0, delta);
    }

    @Test
    public void testSetPower(){
        TrackModel _tm = mock(TrackModel.class);
        testTrainObject = new Train(1, 0, _tm, 1);
        Train spyTrain = spy(testTrainObject);
        doReturn(0.5).when(spyTrain).getGrade();
        spyTrain.setPower(100000,0);
        assertEquals(spyTrain.getPower(),100000.00, delta);
    }

    @Test
    public void testSetPowerNegative(){
        TrackModel _tm = mock(TrackModel.class);
        testTrainObject = new Train(1, 0, _tm, 1);
        Train spyTrain = spy(testTrainObject);
        doReturn(0.5).when(spyTrain).getGrade();
        spyTrain.setPower(-100000,0);
        assertEquals(spyTrain.getPower(),0, delta);
    }

    @Test
    public void testSetPowerTooLarge(){
        TrackModel _tm = mock(TrackModel.class);
        testTrainObject = new Train(1, 0, _tm, 1);
        Train spyTrain = spy(testTrainObject);
        doReturn(0.5).when(spyTrain).getGrade();
        spyTrain.setPower(300000,0);
        assertEquals(spyTrain.getPower(), 120000, delta);
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
    public void testGetEmergencyBrakes(){
        assertEquals(testTrainObject.getEmergencyBrakes(), false);
    }

    @Test
    public void testSetEmergencyBrakes(){
        //turn on
        testTrainObject.setEmergencyBrakes(true);
        assertEquals(testTrainObject.getEmergencyBrakes(), true);
        //turn off
        testTrainObject.setEmergencyBrakes(false);
        assertEquals(testTrainObject.getEmergencyBrakes(), false);
    }

    @Test
    public void testGetEmergencyBrakeRate(){
        assertEquals(-2.73, testTrainObject.getEmergencyBrakeRate(), delta);
    }

    @Test
    public void testGetServiceBrakeRate(){
        assertEquals(-1.2, testTrainObject.getServiceBrakeRate(), delta);
    }

    @Test
    public void testUpdateSpeed(){
        TrackModel _tm = mock(TrackModel.class);

        testTrainObject = new Train(1, 0, _tm, 1);
        testTrainObject.velocity = 10.0;
        testTrainObject.numPassengers = 444;

        Train spyTrain = spy(testTrainObject);
        doReturn(0.5).when(spyTrain).getGrade();


        spyTrain.updateSpeed(10000);
        assertEquals(10.111703, spyTrain.getCurrentVelocity(), delta);

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
        Environment.clock = 10;
        testTrainObject.time = 5;
        testTrainObject.acceleration = .5;
        testTrainObject.velocity = 10;
        TrainController controller = mock(TrainController.class);
        when(controller.getPower()).thenReturn(10000.0);
        testTrainObject.numPassengers = 444;
        Train spyTrain = spy(testTrainObject);
        doReturn(0.5).when(spyTrain).getGrade();
        assertEquals(spyTrain.getDisplacement(), 49.5977, delta);

    }




}
