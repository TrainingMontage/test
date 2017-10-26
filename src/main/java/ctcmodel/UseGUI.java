package CTCModel;
//import CTCModel.CTCGUI;

public class UseGUI {
    public static void main(String args[]){
        //CTCGUI.createAndShowGUI();
        TrackModel trackModel = new TrackModel();
        TrainModel trainModel = new TrainModel(trackModel);
        CTCGUI guiInstance = new CTCGUI(trackModel, trainModel);
        guiInstance.createAndShowGUI();
    }
}