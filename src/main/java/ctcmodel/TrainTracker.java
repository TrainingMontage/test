package CTCModel;

public class TrainTracker{
    private static int trainID;
    
    TrainTracker(){
        //trackModel = trackModelA;
        trainID = 0;
    }
    
    public static int createTrain(int startingBlockID){
        TrackModel.addTrain(startingBlockID);
        return ++trainID;
    }
}