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
 * @author Isaac Goss
 */
package wayside;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import shared.Suggestion;
import wayside.TrackModel;
import wayside.WaysideController;

public class WaysideTest {
    //@Before
    public void init_TrackModel() {
        TrackModel.init();
    }

    //@Test
    public void test_TrackModelInit() {
        for (int i = 0; i < TrackModel.TRACK_LEN; i++) {
            Assert.assertFalse(WaysideController.isOccupied(i));
            Assert.assertFalse(WaysideController.getSwitch(i));
            if (i == TrackModel.SWITCH || i == TrackModel.DEFAULT_LEAF) {
                Assert.assertTrue(WaysideController.getSignal(i));
            } else {
                Assert.assertFalse(WaysideController.getSignal(i));
            }
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
    //@Test
    public void test_emptyTrackSafeSuggestion() {
        int[] list = {0, 1, 2, 3, 4, 5};
        Suggestion s = new Suggestion(0, 10, list);
        Suggestion[] res = {s};
        WaysideController.suggest(res);

        Assert.assertTrue(WaysideController.getSwitch(2));
        Assert.assertEquals(WaysideController.getSpeed(0), 10);
        // Not using switch, signals, or crossing
        Assert.assertTrue(WaysideController.getSwitch(TrackModel.SWITCH));
        Assert.assertTrue(WaysideController.getSignal(TrackModel.SWITCH));
        Assert.assertTrue(WaysideController.getSignal(TrackModel.ACTIVE_LEAF));
        Assert.assertFalse(WaysideController.getSignal(TrackModel.DEFAULT_LEAF));
        Assert.assertFalse(WaysideController.getCrossing(TrackModel.CROSSING));
        for (int i = 0; i < TrackModel.TRACK_LEN; i++) {
            if (i < 6) {
                Assert.assertTrue(WaysideController.getAuthority(i));
            } else {
                Assert.assertFalse(WaysideController.getAuthority(i));
            }
        }
    }

    //@Test
    public void test_trainSafeOnlyNewSuggestion() {
        int[] list = {0, 1, 2, 3};
        Suggestion s = new Suggestion(0, 10, list);
        Suggestion[] res = {s}; // Only giving a suggestion to the new train.
        TrackModel.setOccupancy(4, true); // Put a train on 4.
        WaysideController.suggest(res);

        Assert.assertTrue(WaysideController.isOccupied(4));
        Assert.assertTrue(WaysideController.getSwitch(TrackModel.SWITCH));
        Assert.assertTrue(WaysideController.getSignal(TrackModel.SWITCH));
        Assert.assertTrue(WaysideController.getSignal(TrackModel.ACTIVE_LEAF));
        Assert.assertFalse(WaysideController.getSignal(TrackModel.DEFAULT_LEAF));
        Assert.assertFalse(WaysideController.getCrossing(TrackModel.CROSSING));
        
        Assert.assertEquals(WaysideController.getSpeed(0), 10);
        for (int i = 0; i < TrackModel.TRACK_LEN; i++) {
            if (i < 4) {
                Assert.assertTrue(WaysideController.getAuthority(i));
            } else {
                Assert.assertFalse(WaysideController.getAuthority(i));
            }
        }
    }

    //@Test
    public void test_trainUnsafeOnlyNewSuggestion() {
        int[] list = {0, 1, 2, 3, 4, 5};
        Suggestion s = new Suggestion(0, 10, list);
        Suggestion[] res = {s}; // Only giving a suggestion to the new train.
        TrackModel.setOccupancy(4, true); // Put a train on 4.
        WaysideController.suggest(res);

        Assert.assertTrue(WaysideController.isOccupied(4));
        Assert.assertFalse(WaysideController.getSwitch(TrackModel.SWITCH));
        Assert.assertTrue(WaysideController.getSignal(TrackModel.SWITCH));
        Assert.assertFalse(WaysideController.getSignal(TrackModel.ACTIVE_LEAF));
        Assert.assertTrue(WaysideController.getSignal(TrackModel.DEFAULT_LEAF));
        Assert.assertFalse(WaysideController.getCrossing(TrackModel.CROSSING));
        Assert.assertEquals(WaysideController.getSpeed(0), 0);
        for (int block = 0; block < TrackModel.TRACK_LEN; block++) {
            Assert.assertFalse(WaysideController.getAuthority(block));
        }
    }

    //@Test
    public void test_trainSafeBothSuggestion() {
        int[] auth1 = {0, 1, 2, 3};
        Suggestion s1 = new Suggestion(0, 10, auth1);
        int[] auth2 = {4, 5, 6, 7, 8};
        Suggestion s2 = new Suggestion(4, 10, auth2);
        Suggestion[] res = {s1, s2}; // Only giving a suggestion to the new train.
        TrackModel.setOccupancy(4, true); // Put a train on 4.
        WaysideController.suggest(res);

        Assert.assertTrue(WaysideController.isOccupied(4));
        Assert.assertTrue(WaysideController.getSwitch(TrackModel.SWITCH));
        Assert.assertTrue(WaysideController.getCrossing(TrackModel.CROSSING));
        Assert.assertTrue(WaysideController.getSignal(TrackModel.SWITCH)); // root of the switch
        Assert.assertTrue(WaysideController.getSignal(TrackModel.ACTIVE_LEAF)); // leaf the new train will be on.
        Assert.assertFalse(WaysideController.getSignal(TrackModel.DEFAULT_LEAF)); // the switch will be the other direction.
        Assert.assertEquals(WaysideController.getSpeed(0), 10);
        for (int i = 0; i < TrackModel.TRACK_LEN; i++) {
            // Every block has assigned authority.
            Assert.assertTrue(WaysideController.getAuthority(i));
        }
    }
}