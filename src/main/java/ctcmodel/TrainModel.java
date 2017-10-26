package CTCModel;

public class TrainModel{
    private static TrackModel trackModel;
    
    TrainModel(TrackModel trackModelA){
        trackModel = trackModelA;
    }
    
    public static int createTrain(int startingBlockID){
        trackModel.addTrain(startingBlockID);
        return 1;
    }
}