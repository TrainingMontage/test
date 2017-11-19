package CTCModel;

public class UseGUI {
    public static void main(String args[]){
        //TrackModel trackModel = new TrackModel();
        //TrackModel.init();
        
        TrackModel.initWithTestData();
        WaysideController.init();
        //TrainTracker trainTracker = new TrainTracker();
        CTCModel.init();
        CTCGUI.run();
    }
}