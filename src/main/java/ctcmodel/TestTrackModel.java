

public class TestTrackModel{
    private static TestStaticBlock[] blocks;
    private static int[] status;
    private static boolean[] occupied;
    private static boolean[] signal;
    private static boolean[] crossing;
    private static boolean[] mySwitch;//switch is a reserved word
    
    TestTrackModel(){
        blocks = new TestStaticBlock[4];
        status = new int[4];
        occupied = new boolean[4];
        signal = new boolean[4];
        crossing = new boolean[4];
        mySwitch = new boolean[4];
        
        for(int i = 0; i < 4; i++){
            status[i] = 0;
            occupied[i] = false;
            signal[i] = false;
            crossing[i] = false;
            mySwitch[i] = false;
        }
        blocks[0] = new TestStaticBlock(0, 10, 50, 1.0,
                    100, false, false,
                    false, false, null,
                    false, false);
        blocks[1] = new TestStaticBlock(1, 20, 100, 0.0,
                    100, false, false,
                    false, false, null,
                    true, true);
        blocks[2] = new TestStaticBlock(2, 5, 100, -2.0,
                    -50, true, false,
                    false, true, "Shadyside",
                    false, true);
        blocks[3] = new TestStaticBlock(3, 25, 200, 0.0,
                    -50, true, true,
                    true, false, null,
                    false, true);
    }
    
    public static void addTrain(int blockID){
        occupied[blockID] = true;
    }
    public static TestStaticBlock getStaticBlock(int blockID){
        return blocks[blockID];
    }
    public static int getStatus(int blockID){
        return status[blockID];
    }
    public static boolean isOccupied(int blockID){
        return occupied[blockID];
    }
    public static boolean getSignal(int blockID){
        return signal[blockID];
    }
    public static boolean getCrossing(int blockID){
        return crossing[blockID];
    }
    public static boolean getSwitch(int blockID){
        return mySwitch[blockID];
    }
}