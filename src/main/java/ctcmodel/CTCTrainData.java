package CTCModel;

public class CTCTrainData{
    private int dataTrainID;
    private int dataBlockID;
    private int dataSpeed;
    private String dataAuthority;
    private int dataOrigin;
    private int dataDestination;
    
    protected CTCTrainData(){
        dataTrainID = -1;
        dataBlockID = -1;
        dataSpeed = -1;
        dataAuthority = null;
        dataOrigin = -1;
        dataDestination = -1;
    }
    protected CTCTrainData(int trainID, int blockID, int speed, String authority, int origin, int destination){
        dataTrainID = trainID;
        dataBlockID = blockID;
        dataSpeed = speed;
        dataAuthority = authority;
        dataOrigin = origin;
        dataDestination = destination;
    }
    
    public int getTrainID(){
        return dataTrainID;
    }
    public int getBlockID(){
        return dataBlockID;
    }
    public int getSpeed(){
        return dataSpeed;
    }
    public String getAuthority(){
        return dataAuthority;
    }
    public int getOrigin(){
        return dataOrigin;
    }
    public int getDestination(){
        return dataDestination;
    }
    
    public void setTrainID(int trainID){
        dataTrainID = trainID;
    }
    public void setBlockID(int blockID){
        dataBlockID = blockID;
    }
    public void setSpeed(int speed){
        dataSpeed = speed;
    }
    public void setAuthority(String authority){
        dataAuthority = authority;
    }
    public void setOrigin(int origin){
        dataOrigin = origin;
    }
    public void setDestination(int destination){
        dataDestination = destination;
    }
    
}