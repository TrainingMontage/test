//package CTCModel;
//import CTCModel.CTCGUI;

public class UseGUI {
    public static void main(String args[]){
        //CTCGUI.createAndShowGUI();
        TestTrackModel trackModel = new TestTrackModel();
        TestTrain trainModel = new TestTrain(trackModel);
        CTCGUI guiInstance = new CTCGUI(trackModel, trainModel);
        guiInstance.createAndShowGUI();
    }
}