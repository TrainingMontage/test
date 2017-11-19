package CTCModel;

import java.sql.SQLException;
import shared.BlockStatus;
import java.util.ArrayList;

public class TrackModel{
    private static TrackModel model = null;
    private static StaticTrack staticTrack = null;
    private StaticBlock[] blocks;
    private BlockStatus[] status;
    private boolean[] occupied;
    private boolean[] signal;
    private boolean[] crossing;
    private boolean[] mySwitch;//switch is a reserved word
    
    private TrackModel(){
        blocks = new StaticBlock[4];
        status = new BlockStatus[4];
        occupied = new boolean[4];
        signal = new boolean[4];
        crossing = new boolean[4];
        mySwitch = new boolean[4];
        
        for(int i = 0; i < 4; i++){
            status[i] = BlockStatus.OPERATIONAL;
            occupied[i] = false;
            signal[i] = false;
            crossing[i] = false;
            mySwitch[i] = false;
        }
        blocks[0] = new StaticBlock(0, 10, 50, 1.0,
                    100, false, false,
                    false, false, null,
                    false, false);
        blocks[1] = new StaticBlock(1, 20, 100, 0.0,
                    100, false, false,
                    false, false, null,
                    true, true);
        blocks[2] = new StaticBlock(2, 5, 100, -2.0,
                    -50, true, false,
                    false, true, "Shadyside",
                    false, true);
        blocks[3] = new StaticBlock(3, 25, 200, 0.0,
                    -50, true, true,
                    true, false, null,
                    false, true);
        StaticSwitch ss = new StaticSwitch();
        ss.setRoot(blocks[3]);
        ss.setDefaultLeaf(new StaticBlock(4, 20, 100, 0.0,
                    100, false, false,
                    false, false, null,
                    true, true));
        ss.setActiveLeaf(new StaticBlock(5, 20, 100, 0.0,
                    100, false, false,
                    false, false, null,
                    true, true));
        blocks[3].setStaticSwitch(ss);
        ss.getDefaultLeaf().setStaticSwitch(ss);
        ss.getActiveLeaf().setStaticSwitch(ss);
    }
    
    
    public static TrackModel getTrackModel(){
        if (model == null) {
            model = new TrackModel();
        }
        return model;
    }
    
    public static TrackModel init(){
        if (model == null) {
            model = new TrackModel();
        }
        return model;
    }
    
    public static TrackModel initWithTestData(){
        return init();
    }
    
    public StaticTrack getStaticTrack(){
        return this.staticTrack;
    }
    
    public void addTrain(int blockID){
        occupied[blockID] = true;
    }
    public StaticBlock getStaticBlock(int blockID){
        return blocks[blockID];
    }
    public BlockStatus getStatus(int blockID){
        return status[blockID];
    }
    public boolean isOccupied(int blockID){
        return occupied[blockID];
    }
    public boolean getSignal(int blockID){
        return signal[blockID];
    }
    public boolean getCrossing(int blockID){
        return crossing[blockID];
    }
    public boolean getSwitch(int blockID){
        return mySwitch[blockID];
    }
    public ArrayList<Integer> getBlockIds(){
        ArrayList<Integer> al = new ArrayList<Integer>();
        al.add(0);
        al.add(1);
        al.add(2);
        al.add(3);
        return al;
    }
}