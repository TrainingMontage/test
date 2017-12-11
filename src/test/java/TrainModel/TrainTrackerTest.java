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

public class TrainTrackerTest{
    TrainTracker tracker;
    @Test
    public void testCreate(){
        tracker = new TrainTracker();
        //create train test
        int test1 = tracker.getTrainTracker().createTrainTest(0);
        int test2 = tracker.getTrainTracker().createTrainTest(0);
        int test3 = tracker.getTrainTracker().createTrainTest(0);
        assertEquals(0, test1);
        assertEquals(1, test2);
        assertEquals(2, test3);
        //get train test
        int get1 = tracker.getTrainTracker().getTrainTest(test1).getTrainId();
        int get2 = tracker.getTrainTracker().getTrainTest(test2).getTrainId();
        int get3 = tracker.getTrainTracker().getTrainTest(test3).getTrainId();
        assertEquals(0, get1);
        assertEquals(1, get2);
        assertEquals(2, get3);
        //get size test
        int size = tracker.getSize();
        assertEquals(3, size);
        //remove train test
        tracker.getTrainTracker().removeTrain(0);
        int check = tracker.getTrainTracker().getSize();
        assertEquals(2, check);
        tracker.getTrainTracker().removeTrain(2);
        check = tracker.getTrainTracker().getSize();
        assertEquals(1, check);
        tracker.getTrainTracker().removeTrain(1);
        check = tracker.getTrainTracker().getSize();
        assertEquals(0, check);

    }

}
