package CTCModel;
//import CTCModel.CTCGUI;

public class UseGUI {
    public static void main(String args[]){
        //CTCGUI.createAndShowGUI();
        //TrackModel trackModel = new TrackModel();
        //TrackModel.init();
        try{
            TrackModel.initWithTestData();
        }catch(Exception e){
        
        }
        WaysideController.init();
        //TrainModel trainModel = new TrainModel(trackModel);
        TrainModel trainModel = new TrainModel();
        //CTCGUI guiInstance = new CTCGUI(trackModel, trainModel);
        //CTCGUI guiInstance = new CTCGUI(trainModel);
        //guiInstance.createAndShowGUI();
        CTCModel.init(trainModel);
        CTCGUI.createAndShowGUI();
    }
}