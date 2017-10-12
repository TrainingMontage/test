
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

    /** 
     * No train on track, new one comming from yard, 
     * safe speed and authority suggested. 
     * Switch needs to be set after;
     * all of the authorities should be set;
     * only the first block should be occupied.
     */
    @Test public void test_emptyTrackSafeSuggestion() {
        int[] list = {0, 1, 2, 3, 4, 5};
        Suggestion s = new Suggestion(0, 10, list);
        Suggestion[] res = {s};
        WaysideController.suggest(res);
        Assert.assertTrue(MockTrack.getSwitch(2));
        for (int i = 0; i < MockTrack.TRACK_LEN; i++) {
            if (i < 6) {
                Assert.assertTrue(MockTrack.getTrainAuthority(i));
            } else {
                Assert.assertFalse(MockTrack.getTrainAuthority(i));
            }
            if (i == 0) {
                Assert.assertTrue(MockTrack.isOccupied(i));
            } else {
                Assert.assertFalse(MockTrack.isOccupied(i));
            }
        }
    }
}