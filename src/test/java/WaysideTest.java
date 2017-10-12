
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
            Assert.assertFalse(TrackModel.isOccupied(i));
            Assert.assertFalse(TrackModel.getSwitch(i));
            Assert.assertFalse(TrackModel.getSignal(i));
            Assert.assertFalse(TrackModel.getCrossing(i));
            Assert.assertFalse(TrackModel.getTrainAuthority(i));
            Assert.assertEquals(TrackModel.getTrainSpeed(i), 0);
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
        Assert.assertTrue(TrackModel.getSwitch(2));
        for (int i = 0; i < TrackModel.TRACK_LEN; i++) {
            if (i < 6) {
                Assert.assertTrue(TrackModel.getTrainAuthority(i));
            } else {
                Assert.assertFalse(TrackModel.getTrainAuthority(i));
            }
            if (i == 0) {
                Assert.assertTrue(TrackModel.isOccupied(i));
            } else {
                Assert.assertFalse(TrackModel.isOccupied(i));
            }
        }
    }
}