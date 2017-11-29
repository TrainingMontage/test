/*   ______                 _           _
 *  /_  __/ _____  ____ _  (_) ____    (_) ____    ____ _
 *   / /   / ___/ / __ `/ / / / __ \  / / / __ \  / __ `/
 *  / /   / /    / /_/ / / / / / / / / / / / / / / /_/ /
 * /_/   /_/     \__,_/ /_/ /_/ /_/ /_/ /_/ /_/  \__, /
 *                                              /____/
 *     __  ___                 __
 *    /  |/  / ____    ____   / /_  ____ _  ____ _  ___
 *   / /|_/ / / __ \  / __ \ / __/ / __ `/ / __ `/ / _ \
 *  / /  / / / /_/ / / / / // /_  / /_/ / / /_/ / /  __/
 * /_/  /_/  \____/ /_/ /_/ \__/  \__,_/  \__, /  \___/
 *                                       /____/
 *
 * @author Mitchell Moran
 */


package CTCModel;

import java.util.ArrayList;

public class CTCTrainData{
    private int dataTrainID;
    private int dataBlockID;
    private double dataSpeed;
    private String dataAuthority;
    private int dataOrigin;
    private int dataDestination;
    private ArrayList<Integer> history;
    
    protected CTCTrainData(){
        dataTrainID = -1;
        dataBlockID = -1;
        dataSpeed = -1;
        dataAuthority = null;
        dataOrigin = -1;
        dataDestination = -1;
        history = new ArrayList<Integer>();
    }
    protected CTCTrainData(int trainID, int blockID, double speed, String authority, int origin, int destination){
        dataTrainID = trainID;
        dataBlockID = blockID;
        dataSpeed = speed;
        dataAuthority = authority;
        dataOrigin = origin;
        dataDestination = destination;
        history = new ArrayList<Integer>();
    }
    
    public int getTrainID(){
        return dataTrainID;
    }
    public int getBlockID(){
        return dataBlockID;
    }
    public double getSpeed(){
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
    public void setSpeed(double speed){
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
    public void historyAdd(int bId){
        history.add(0,bId);
    }
    public int historyGet(int index){
        return history.get(index);
    }
    public void historyRemove(int index){
        history.remove(index);
    }
    public int historySize(){
        return history.size();
    }
    
}