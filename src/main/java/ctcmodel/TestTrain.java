

public class TestTrain{
    private static TestTrackModel trackModel;
    
    TestTrain(TestTrackModel trackModelA){
        trackModel = trackModelA;
    }
    
    public static int createTrain(int startingBlockID){
        trackModel.addTrain(startingBlockID);
        return 1;
    }
}