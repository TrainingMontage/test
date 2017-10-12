
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import utils.Suggestion;
import wayside.TrackModel;
import wayside.WaysideController;

public class WaysideTest {
    @Before
    public void init_TrackModel() {
        TrackModel.init();
    }

    @Test
    public void test_TrackModelInit() {
        for (int i = 0; i < TrackModel.TRACK_LEN; i++) {
            Assert.assertFalse(WaysideController.isOccupied(i));
            Assert.assertFalse(WaysideController.getSwitch(i));
            Assert.assertFalse(WaysideController.getSignal(i));
            Assert.assertFalse(WaysideController.getCrossing(i));
            Assert.assertFalse(WaysideController.getAuthority(i));
            Assert.assertEquals(WaysideController.getSpeed(i), 0);
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
        Assert.assertTrue(WaysideController.getSwitch(2));
        for (int i = 0; i < TrackModel.TRACK_LEN; i++) {
            if (i < 6) {
                Assert.assertTrue(WaysideController.getAuthority(i));
            } else {
                Assert.assertFalse(WaysideController.getAuthority(i));
            }
            if (i == 0) {
                Assert.assertTrue(WaysideController.isOccupied(i));
            } else {
                Assert.assertFalse(WaysideController.isOccupied(i));
            }
        }
    }
}