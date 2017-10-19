/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traincontroller;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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

//    /**
//     * Test of main method, of class TrainController.
//     */
//    @Test
//    public void testMain() throws Exception {
//        System.out.println("main");
//        String[] args = null;
//        TrainController.main(args);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

//    /**
//     * Test of runTest method, of class TrainController.
//     */
//    @Test
//    public void testRunTest() throws Exception {
//        System.out.println("runTest");
//        int period = 0;
//        TrainController.runTest(period);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of computeSafeBrake method, of class TrainController.
     */
    @Test
    public void testComputeSafeBrake() {
        System.out.println("computeSafeBrake");
        TrainController instance = new TrainController(true);
        double expResult = 240.0;
        double result = instance.computeSafeBrake();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
//        fail("computeSafeBrake failure.");
    }

    /**
     * Test of computeSafeSpeed method, of class TrainController.
     */
    @Test
    public void testComputeSafeSpeed() {
        System.out.println("computeSafeSpeed");
        TrainController instance = new TrainController(true);
        double expResult = 24.0;
        double result = instance.computeSafeSpeed();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
//        fail("computeSafeBrake failure.");
    }

    /**
     * Test of getPower method, of class TrainController.
     */
    @Test
    public void testGetPower() {
        System.out.println("getPower");
        TrainController instance = new TrainController(true);
        double expResult = 2700.0;
        double result = instance.getPower();
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
//        fail("getPower failure.");
    }
    
     /**
     * Test of getPower method, of class TrainController.
     */
    @Test
    public void testGetPowerLoop() {
        System.out.println("getPower loop");
        TrainController instance = new TrainController(true);
        for(int i = 0; i < 200; i++) {
            instance.theTrain.setPower(instance.getPower());
        }
        double result = instance.theTrain.getVelocity();
        assertEquals(24.0, result, 2.0);
        // TODO review the generated test code and remove the default call to fail.
//        fail("getPower failure.");
    }

//    /**
//     * Test of setKu method, of class TrainController.
//     */
//    @Test
//    public void testSetKu() {
//        System.out.println("setKu");
//        double newVal = 0.0;
//        TrainController instance = new TrainController(true);
//        instance.setKu(newVal);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of setLights method, of class TrainController.
     */
    @Test
    public void testSetLightsOn() {
        System.out.println("setLightsOn");
        boolean on = true;
        TrainController instance = new TrainController(true);
        boolean expResult = true;
        boolean result = instance.setLights(on);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("setLights(on) failure.");
    }
    
    /**
     * Test of setLights method, of class TrainController.
     */
    @Test
    public void testSetLightsOff() {
        System.out.println("setLightsOff");
        boolean on = false;
        TrainController instance = new TrainController(true);
        boolean expResult = false;
        boolean result = instance.setLights(on);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("setLights(off) failure.");
    }

    /**
     * Test of setDoors method, of class TrainController.
     */
    @Test
    public void testSetDoors0() {
        System.out.println("setDoors");
        boolean leftOpen = false;
        boolean rightOpen = false;
        TrainController instance = new TrainController(true);
        byte expResult = 0;
        byte result = instance.setDoors(leftOpen, rightOpen);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("setDoors(both closed) failure.");
    }
    
    /**
     * Test of setDoors method, of class TrainController.
     */
    @Test
    public void testSetDoors1() {
        System.out.println("setDoors");
        boolean leftOpen = false;
        boolean rightOpen = true;
        TrainController instance = new TrainController(true);
        byte expResult = 1;
        byte result = instance.setDoors(leftOpen, rightOpen);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("setDoors(closed, open) failure.");
    }
    
    /**
     * Test of setDoors method, of class TrainController.
     */
    @Test
    public void testSetDoors2() {
        System.out.println("setDoors");
        boolean leftOpen = true;
        boolean rightOpen = false;
        TrainController instance = new TrainController(true);
        byte expResult = 2;
        byte result = instance.setDoors(leftOpen, rightOpen);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("setDoors(open, closed) failure.");
    }
    
    /**
     * Test of setDoors method, of class TrainController.
     */
    @Test
    public void testSetDoors3() {
        System.out.println("setDoors");
        boolean leftOpen = true;
        boolean rightOpen = true;
        TrainController instance = new TrainController(true);
        byte expResult = 3;
        byte result = instance.setDoors(leftOpen, rightOpen);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("setDoors(both open) failure.");
    }

    /**
     * Test of justStop method, of class TrainController.
     */
    @Test
    public void testJustStop() {
        System.out.println("justStop");
        TrainController instance = new TrainController(true);
        boolean expResult = true;
        boolean result = instance.justStop();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("justStop failure.");
    }

    /**
     * Test of youCanGoNow method, of class TrainController.
     */
    @Test
    public void testYouCanGoNow() {
        System.out.println("youCanGoNow");
        TrainController instance = new TrainController(true);
        boolean expResult = false;
        boolean result = instance.youCanGoNow();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("youCanGoNow failure.");
    }

    /**
     * Test of sendForService method, of class TrainController.
     */
    @Test
    public void testSendForService() {
        System.out.println("sendForService");
        TrainController instance = new TrainController(true);
        boolean expResult = true;
        boolean result = instance.sendForService();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("sendForService failure.");
    }

    /**
     * Test of displayStation method, of class TrainController.
     */
    @Test
    public void testDisplayStation() {
        System.out.println("displayStation");
        String name = "Test Passes";
        TrainController instance = new TrainController(true);
        instance.displayStation(name);
        assertEquals(name, instance.station);
        // TODO review the generated test code and remove the default call to fail.
//        fail("displayStation failure.");
    }
    
}
