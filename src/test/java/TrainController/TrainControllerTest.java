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
    
//    public TrainControllerTest() {
//    }
//    
//    @BeforeClass
//    public static void setUpClass() {
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
//    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }
//
////    /**
////     * Test of main method, of class TrainController.
////     */
////    @Test
////    public void testMain() throws Exception {
////        System.out.println("main");
////        String[] args = null;
////        TrainController.main(args);
////        // TODO review the generated test code and remove the default call to fail.
////        fail("The test case is a prototype.");
////    }
//
////    /**
////     * Test of runTest method, of class TrainController.
////     */
////    @Test
////    public void testRunTest() throws Exception {
////        System.out.println("runTest");
////        int period = 0;
////        TrainController.runTest(period);
////        // TODO review the generated test code and remove the default call to fail.
////        fail("The test case is a prototype.");
////    }
//
//    /**
//     * Test of computeSafeBrake method, of class TrainController.
//     */
//    @Test
//    public void testComputeSafeBrake000() {
//        System.out.println("computeSafeBrake");
//        TrainController instance = new TrainController(true, 1);
//        instance.theTrain.setSpeed(0.0);
//        double expResult = 0.0;
//        double result = instance.computeSafeBrake();
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("computeSafeBrake failure.");
//    }
//    
//     /**
//     * Test of computeSafeBrake method, of class TrainController.
//     */
//    @Test
//    public void testComputeSafeBrake001() {
//        System.out.println("computeSafeBrake");
//        TrainController instance = new TrainController(true, 1);
//        double expResult = 240.0;
//        double result = instance.computeSafeBrake();
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("computeSafeBrake failure.");
//    }
//
//    /**
//     * Test of computeSafeSpeed method, of class TrainController.
//     */
//    @Test
//    public void testComputeSafeSpeed000() {
//        System.out.println("computeSafeSpeed");
//        TrainController instance = new TrainController(true, 1);
//        instance.theTrain.setSuggested(0.0);
//        double expResult = 0.0;
//        double result = instance.computeSafeSpeed();
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("computeSafeBrake failure.");
//    }
//    
//    /**
//     * Test of computeSafeSpeed method, of class TrainController.
//     */
//    @Test
//    public void testComputeSafeSpeed001() {
//        System.out.println("computeSafeSpeed");
//        TrainController instance = new TrainController(true, 1);
//        double expResult = 24.0;
//        double result = instance.computeSafeSpeed();
//        assertEquals(expResult, result, 0.0);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("computeSafeBrake failure.");
//    }
//
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
        StaticBlock _block = mock(StaticBlock.class);
        StaticBlock _block = new StaticBlock();
        TrainController instance = new TrainController(_train, 1, false);
        instance.setStaticBlock(_block);
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
        instance.setStaticBlock(_block);
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
        double expResult = 2700.0;
        double result = instance.getPower();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
//        fail("getPower failure.");
    }
    
     /**
     * Test of getPower method, of class TrainController.
     */
//    @Test
//    public void testGetPower002() {
//        System.out.println("getPower loop");
//        Train _train = mock(Train.class);
//        TrainController instance = new TrainController(_train, 1);
//        doReturn(0.0).when(_train).getSuggestedSpeed();
//        doReturn(0.0).when(_train).getCurrentVelocity();
//        doReturn(120000.0).when(_train).getMaxPower();
//        doReturn(-1.2).when(_train).getServiceBrakeRate();
//        doReturn(-2.73).when(_train).getEmergencyBrakeRate();
//        for(int i = 0; i < 200; i++) {
//            instance.theTrain.setPower(instance.getPower());
//        }
//        double result = instance.theTrain.getCurrentVelocity();
//        assertEquals(24.0, result, 1.0);
        // TODO review the generated test code and remove the default call to fail.
//        fail("getPower failure.");
//    }
//
////    /**
////     * Test of setKu method, of class TrainController.
////     */
////    @Test
////    public void testSetKu() {
////        System.out.println("setKu");
////        double newVal = 0.0;
////        TrainController instance = new TrainController(true, 1);
////        instance.setKu(newVal);
////        // TODO review the generated test code and remove the default call to fail.
////        fail("The test case is a prototype.");
////    }
//
//    /**
//     * Test of setLights method, of class TrainController.
//     */
//    @Test
//    public void testSetLights000() {
//        System.out.println("setLightsOn");
//        boolean on = true;
//        TrainController instance = new TrainController(true, 1);
//        boolean expResult = true;
//        boolean result = instance.setLights(on);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("setLights(on) failure.");
//    }
//    
//    /**
//     * Test of setLights method, of class TrainController.
//     */
//    @Test
//    public void testSetLights001() {
//        System.out.println("setLightsOff");
//        boolean on = false;
//        TrainController instance = new TrainController(true, 1);
//        boolean expResult = false;
//        boolean result = instance.setLights(on);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("setLights(off) failure.");
//    }
//
//    /**
//     * Test of setDoors method, of class TrainController.
//     */
//    @Test
//    public void testSetDoors000() {
//        System.out.println("setDoors");
//        boolean leftOpen = false;
//        boolean rightOpen = false;
//        TrainController instance = new TrainController(true, 1);
//        byte expResult = 0;
//        byte result = instance.setDoors(leftOpen, rightOpen);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("setDoors(both closed) failure.");
//    }
//    
//    /**
//     * Test of setDoors method, of class TrainController.
//     */
//    @Test
//    public void testSetDoors001() {
//        System.out.println("setDoors");
//        boolean leftOpen = false;
//        boolean rightOpen = true;
//        TrainController instance = new TrainController(true, 1);
//        byte expResult = 1;
//        byte result = instance.setDoors(leftOpen, rightOpen);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("setDoors(closed, open) failure.");
//    }
//    
//    /**
//     * Test of setDoors method, of class TrainController.
//     */
//    @Test
//    public void testSetDoors002() {
//        System.out.println("setDoors");
//        boolean leftOpen = true;
//        boolean rightOpen = false;
//        TrainController instance = new TrainController(true, 1);
//        byte expResult = 2;
//        byte result = instance.setDoors(leftOpen, rightOpen);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("setDoors(open, closed) failure.");
//    }
//    
//    /**
//     * Test of setDoors method, of class TrainController.
//     */
//    @Test
//    public void testSetDoors003() {
//        System.out.println("setDoors");
//        boolean leftOpen = true;
//        boolean rightOpen = true;
//        TrainController instance = new TrainController(true, 1);
//        byte expResult = 3;
//        byte result = instance.setDoors(leftOpen, rightOpen);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("setDoors(both open) failure.");
//    }
//
//    /**
//     * Test of justStop method, of class TrainController.
//     */
//    @Test
//    public void testJustStop000() {
//        System.out.println("justStop");
//        TrainController instance = new TrainController(true, 1);
//        boolean expResult = true;
//        boolean result = instance.justStop();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("justStop failure.");
//    }
//
//    /**
//     * Test of youCanGoNow method, of class TrainController.
//     */
//    @Test
//    public void testYouCanGoNow000() {
//        System.out.println("youCanGoNow");
//        TrainController instance = new TrainController(true, 1);
//        boolean expResult = false;
//        boolean result = instance.youCanGoNow();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("youCanGoNow failure.");
//    }
//
//    /**
//     * Test of sendForService method, of class TrainController.
//     */
//    @Test
//    public void testSendForService() {
//        System.out.println("sendForService");
//        TrainController instance = new TrainController(true, 1);
//        boolean expResult = true;
//        boolean result = instance.sendForService();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("sendForService failure.");
//    }
//
//    /**
//     * Test of displayStation method, of class TrainController.
//     */
//    @Test
//    public void testDisplayStation() {
//        System.out.println("displayStation");
//        String name = "Test Passes";
//        TrainController instance = new TrainController(true, 1);
//        instance.displayStation(name);
//        assertEquals(name, instance.station);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("displayStation failure.");
//    }
//    
//    /**
//     * Test of getID method, of class TrainController.
//     */
//    @Test
//    public void testGetID000() {
//        System.out.println("GetID");
//        TrainController instance = new TrainController(true, 1);
//        int expResult = 1;
//        int result = instance.getID();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("displayStation failure.");
//    }
//    
//    /**
//     * Test of getID method, of class TrainController.
//     */
//    @Test
//    public void testGetID001() {
//        System.out.println("GetID");
//        TrainController instance = new TrainController(true, 2);
//        int expResult = 2;
//        int result = instance.getID();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("displayStation failure.");
//    }
//    
//    /**
//     * Test of getID method, of class TrainController.
//     */
//    @Test
//    public void testGetID002() {
//        System.out.println("GetID");
//        TrainController instance = new TrainController(true, -1);
//        int expResult = -1;
//        int result = instance.getID();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("displayStation failure.");
//    }
//    
//    /**
//     * Test of getID method, of class TrainController.
//     */
//    @Test
//    public void testGetID003() {
//        System.out.println("GetID");
//        TrainController instance = new TrainController(true, 0);
//        int expResult = 0;
//        int result = instance.getID();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("displayStation failure.");
//    }
//    
//    /**
//     * Test of getTrainControllerByID method, of class TrainController.
//     */
//    @Test
//    public void testGetTrainControllerByID000() {
//        System.out.println("GetTrainControllerByID");
//        TrainController instance = new TrainController(true, 1);
//        for(int i = 2; i < 5; i++) {
//            instance.addTrainController(new TrainController(true, i));
//        }
//        int expResult = 3;
//        TrainController result = instance.getTrainControllerByID(expResult);
//        assertEquals(expResult, result.getID());
//        // TODO review the generated test code and remove the default call to fail.
////        fail("displayStation failure.");
//    }
//    
//    /**
//     * Test of getTrainControllerByID method, of class TrainController.
//     */
//    @Test
//    public void testGetTrainControllerByID001() {
//        System.out.println("GetTrainControllerByID");
//        TrainController instance = new TrainController(true, 1);
//        for(int i = 2; i < 5; i++) {
//            instance.addTrainController(new TrainController(true, i));
//        }
//        TrainController result = instance.getTrainControllerByID(10);
//        assertEquals(null, result);
//        // TODO review the generated test code and remove the default call to fail.
////        fail("displayStation failure.");
//    }
//    
//    /**
//     * Test of getTrainControllerByID method, of class TrainController.
//     */
//    @Test
//    public void testGetTrainControllerByID002() {
//        System.out.println("GetTrainControllerByID");
//        TrainController instance = new TrainController(true, 1);
//        TrainController result = instance.getTrainControllerByID(1);
//        assertEquals(instance.getID(), result.getID());
//        // TODO review the generated test code and remove the default call to fail.
////        fail("displayStation failure.");
//    }
}
