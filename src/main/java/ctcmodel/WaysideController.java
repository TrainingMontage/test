package CTCModel;

import java.sql.SQLException;
import shared.BlockStatus;
import shared.Suggestion;

public class WaysideController{
    
    private WaysideController(){
        return;
    }
    
    public static void init(){
        return;
    }
    
    public static void suggest(Suggestion[] suggestion) {
        return;
    }
    
    public static StaticBlock getStaticBlock(int blockID){
        return TrackModel.getTrackModel().getStaticBlock(blockID);
    }
    public static BlockStatus getStatus(int blockID){
        return TrackModel.getTrackModel().getStatus(blockID);
    }
    public static boolean isOccupied(int blockID){
        return TrackModel.getTrackModel().isOccupied(blockID);
    }
    public static boolean getSignal(int blockID){
        return TrackModel.getTrackModel().getSignal(blockID);
    }
    public static boolean getCrossing(int blockID){
        return TrackModel.getTrackModel().getCrossing(blockID);
    }
    public static boolean getSwitch(int blockID){
        return TrackModel.getTrackModel().getSwitch(blockID);
    }
}