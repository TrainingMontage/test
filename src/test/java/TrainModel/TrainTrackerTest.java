package trainmodel;


import org.junit.*;
import org.junit.rules.*;
import static org.junit.Assert.*;

public class TrainTrackerTest{
    @Test
    public void testCreateAndGet(){
        TrainTracker tracker = TrainTracker.getTrainTracker();
        //create 3 new trains with starting block of 0
        tracker.createTrain(0);
        tracker.createTrain(0);
        tracker.createTrain(0);
        assertEquals(tracker.getTrain(0).getTrainId(), 0);
        assertEquals(tracker.getTrain(1).getTrainId(), 1);
        assertEquals(tracker.getTrain(2).getTrainId(), 2);
    }
}
