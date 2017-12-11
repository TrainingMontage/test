package trainmodel;


import org.junit.*;
import org.junit.rules.*;
import static org.junit.Assert.*;

public class TrainTrackerTest{
    TrainTracker tracker;
    @Test
    public void testCreate(){
        tracker = new TrainTracker();
        int test1 = tracker.getTrainTracker().createTrainTest(0);
        int test2 = tracker.getTrainTracker().createTrainTest(0);
        int test3 = tracker.getTrainTracker().createTrainTest(0);
        int get1 = tracker.getTrainTracker().getTrain(test1).getTrainId();
        int get2 = tracker.getTrainTracker().getTrain(test2).getTrainId();
        int get3 = tracker.getTrainTracker().getTrain(test3).getTrainId();
        int size = tracker.getSize();

        assertEquals(0, test1);
        assertEquals(1, test2);
        assertEquals(2, test3);
        assertEquals(0, get1);
        assertEquals(1, get2);
        assertEquals(2, get3);
        assertEquals(3, size);
        /*
        tracker.getTrainTracker().removeTrain(1);
        int check = tracker.getTrainTracker().getSize();
        assertEquals(2, check);
        tracker.getTrainTracker().removeTrain(2);
        check = tracker.getTrainTracker().getSize();
        assertEquals(1, check);
        tracker.getTrainTracker().removeTrain(3);
        check = tracker.getTrainTracker().getSize();
        assertEquals(0, check);
        */

    }

}
