
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import utils.Suggestion;
import wayside.MockTrack;
import wayside.WaysideController;

public class WaysideTest {
    @Before
    public void init_MockTrack() {
        MockTrack.init();
    }

    @Test
    public void test_MockTrackInit() {
        for (int i = 0; i < MockTrack.TRACK_LEN; i++) {
            Assert.assertFalse(MockTrack.isOccupied(i));
            Assert.assertFalse(MockTrack.getSwitch(i));
            Assert.assertFalse(MockTrack.getSignal(i));
            Assert.assertFalse(MockTrack.getCrossing(i));
            Assert.assertFalse(MockTrack.getTrainAuthority(i));
            Assert.assertEquals(MockTrack.getTrainSpeed(i), 0);
        }
    }
}