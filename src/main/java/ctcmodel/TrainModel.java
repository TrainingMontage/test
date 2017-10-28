package CTCModel;

public class TrainModel{
    //private static TrackModel trackModel;
    private static int trainID;
    
    TrainModel(){
        //trackModel = trackModelA;
        trainID = 0;
    }
    
    public static int createTrain(int startingBlockID){
        TrackModel.addTrain(startingBlockID);
        return ++trainID;
    }
}