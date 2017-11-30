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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.Mock;

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
public class TrainControllerTest {
    
    /**
     * Test of getPower method, of class TrainController.
     */
    @Test
    public void testGetPower000() {
        System.out.println("getPower");
//        instance.theTrain.setSpeed(0.0);
//        instance.theTrain.setSuggested(0.0);
        Train _train = mock(Train.class);
        StaticBlock _block = mock(StaticBlock.class);
//        Train _train = new Train(1, 1);
//        StaticBlock _block = new StaticBlock();
        TrainController instance = new TrainController(false, _train, 1);
        instance.theMap.setCurrentBlock(_block);
//        instance.setStaticBlock(_block);
        instance.setKu(500);
        instance.setT(1.0);
        doReturn(-1).when(_train).getBeacon();
        doReturn(0.0).when(_train).getSuggestedSpeed();
        doReturn(0.0).when(_train).getCurrentVelocity();
        doReturn(120000.0).when(_train).getMaxPower();
        doReturn(-1.2).when(_train).getServiceBrakeRate();
        doReturn(-2.73).when(_train).getEmergencyBrakeRate();
        doReturn(false).when(_train).getAuthority();
        
        doReturn(10000.0).when(_block).getLength();
        double expResult = 0.0;
        double result = instance.getPower();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
//        fail("getPower failure.");
    }
    
    /**
     * Test of getPower method, of class TrainController.
     */
    @Test
    public void testGetPower001() {
        System.out.println("getPower");
        Train _train = mock(Train.class);
//        Train _train = new Train(1, 1);
        StaticBlock _block = mock(StaticBlock.class);
//        StaticBlock _block = new StaticBlock();
        TrainController instance = new TrainController(false, _train, 1);
//        instance.setStaticBlock(_block);
        instance.theMap.setCurrentBlock(_block);
        instance.setKu(500);
        instance.setT(1.0);
        doReturn(-1).when(_train).getBeacon();
        doReturn(24.0).when(_train).getSuggestedSpeed();
        doReturn(12.0).when(_train).getCurrentVelocity();
        doReturn(120000.0).when(_train).getMaxPower();
        doReturn(-1.2).when(_train).getServiceBrakeRate();
        doReturn(-2.73).when(_train).getEmergencyBrakeRate();
        doReturn(false).when(_train).getAuthority();
        
        doReturn(10000.0).when(_block).getLength();
        double expResult = 2429.9999999999995;
        double result = instance.getPower();
        assertEquals(expResult, result, 0.0);
	}
}
