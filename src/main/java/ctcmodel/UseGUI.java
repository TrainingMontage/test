package CTCModel;

public class UseGUI {
    public static void main(String args[]){
        //TrackModel trackModel = new TrackModel();
        //TrackModel.init();
        try{
            TrackModel.initWithTestData();
        }catch(Exception e){
        
        }
        WaysideController.init();
        TrainTracker trainTracker = new TrainTracker();
        CTCModel.init(trainTracker);
        CTCGUI.createAndShowGUI();
    }
}