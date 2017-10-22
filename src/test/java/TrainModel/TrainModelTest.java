package trainmodel;


import org.junit.*;
import org.junit.rules.*;
import static org.junit.Assert.*;

public class TrainModelTest{

    @Test
    public void testSetPower(){
        Train trainObject = new Train();
        trainObject.setPower(100000);
        System.out.println(trainObject.getPower());
        System.out.flush();
        assertEquals(trainObject.getPower(),100000);
    }
    @Test
    public void testSetPowerNegative(){
        Train trainObject = new Train();
        trainObject.setPower(-100000);
        assertEquals(trainObject.getPower(),0);
    }
    @Test
    public void testSetPowerTooLarge(){
        Train trainObject = new Train();
        trainObject.setPower(300000);
        assertEquals(trainObject.getPower(), 120000);
    }
}
