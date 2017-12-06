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
import java.util.Iterator;
import shared.BlockStatus;
import shared.Suggestion;
import shared.Environment;
import wayside.WaysideController;
import trackmodel.TrackModel;
import trackmodel.StaticBlock;
import trackmodel.StaticSwitch;
import trackmodel.StaticTrack;
import trainmodel.TrainTracker;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class CTCModel{
    private static CTCModel model = null;
    //private static TrainTracker trainTracker;
    //private static boolean dataValid;
    //private static ArrayList<Integer> dataTrainID;
    //private static ArrayList<Integer> dataBlockID;
    //private static ArrayList<Integer> dataSpeed;
    //private static ArrayList<String> dataAuthority;
    //private static ArrayList<Integer> dataOrigin;
    //private static ArrayList<Integer> dataDestination;
    private static ArrayList<Suggestion> suggestions;
    private static ArrayList<CTCTrainData> trainData;
    //private static JLabel trainLabel;
    private static int last_clock;
    private static StaticTrack track;
    private static ArrayList<Integer> bIds;
    
    //Train-To-Add (TTA) data (to ensure trains aren't added in the middle of an update)
    private static int TTAstartingBlock;
    private static double TTAspeed;
    private static String TTAauthority;
    private static int TTAdestBlock;
    
    //Train-To-Edit (TTE) data (to ensure trains aren't edited in the middle of an update)
    private static int TTEtrainID;
    private static double TTEspeed;
    private static String TTEauthority;
    private static int TTEdestBlock;
    
    private CTCModel(){
        //trainTracker = atrainTracker;
        last_clock = 0;
        suggestions = new ArrayList<Suggestion>();
        trainData = new ArrayList<CTCTrainData>();
        TTAauthority = null;
    }
    
    public static void init(){
        if (model == null) {
            model = new CTCModel();
        }
        track = TrackModel.getTrackModel().getStaticTrack();
        bIds = TrackModel.getTrackModel().getBlockIds();
        CTCGUI.init();
    }
    
    static int checkTrainInputs(int line, String speed, String authority, String destination){
        int tempint;
        double tempdoub;
        //line test
        if(line != 152){//have to spawn next to the yard TODO: make this not fixed ID
            return 1;
        }
        //speed test
        try{
            tempdoub = Double.parseDouble(speed);
        }catch(NumberFormatException ex){
            return 2;
        }
        //authority test
        if(authority.length() != 0){
            //no authority (empty string) is valid
            try{
                for(String blockID: authority.split(",")){
                    //TODO: add a stricter test? do those blockIDs exist? check for contiguous?
                    tempint = Integer.parseInt(blockID.trim());
                }
            }catch(NumberFormatException ex){
                return 3;
            }
        }
        //destination test
        try{
            tempint = Integer.parseInt(destination);
        }catch(NumberFormatException ex){
            return 4;
        }
        return 0;
    }
    
    public static void createTrain(int startingBlockID, double suggestedSpeed,
                                   String suggestedAuth, int destBlockID){
        /*int trainID = TrainTracker.getTrainTracker().createTrain(startingBlockID);
        trainData.add(new CTCTrainData(trainID, startingBlockID, suggestedSpeed,
                                       suggestedAuth, startingBlockID, destBlockID));
        addSuggestion(trainID, suggestedSpeed, suggestedAuth);
        return trainID;
        */
        TTAauthority = suggestedAuth;
        TTAstartingBlock = startingBlockID;
        TTAspeed = suggestedSpeed;
        TTAdestBlock = destBlockID;
    }
    public static void editTrain(int trainID, double suggestedSpeed,
                                 String suggestedAuth, int destBlockID){
        TTEtrainID = trainID;
        TTEauthority = suggestedAuth;
        TTEspeed = suggestedSpeed;
        TTEdestBlock = destBlockID;
    }
    private static void addTrain(){
        if(TTAauthority != null){
            int trainID = TrainTracker.getTrainTracker().createTrain(TTAstartingBlock);
            trainData.add(new CTCTrainData(trainID, TTAstartingBlock, TTAspeed,
                                           TTAauthority, TTAstartingBlock, TTAdestBlock));
            addSuggestion(trainID, TTAspeed, TTAauthority);
        }
        TTAauthority = null;
    }
    private static void editTrain(){
        if(TTEauthority != null){
            //edit ctc train data
            for (CTCTrainData data: trainData){
                if(data.getTrainID() == TTEtrainID){
                    data.setSpeed(TTEspeed);
                    data.setAuthority(TTEauthority);
                    data.setDestination(TTEdestBlock);
                    break;
                }
            }
            //edit suggestion
            addSuggestion(TTEtrainID, TTEspeed, TTEauthority);
        }
        TTEauthority = null;
    }
    public static void addSuggestion(int trainID, double suggestedSpeed, String suggestedAuthority){
        //there are no checks for input correctness here because checkTrainInputs must be called before using this function
        //System.out.println("((("+suggestedAuthority);
        int blockID = -1;
        for (CTCTrainData data: trainData){
            if(data.getTrainID() == trainID){
                data.setSpeed(suggestedSpeed);
                data.setAuthority(suggestedAuthority);
                blockID = data.getBlockID();
                break;
            }
        }
        //parse auth into int[]
        int[] authorityIntArr;
        if(suggestedAuthority.length() == 0){
            authorityIntArr = new int[0];
        }else{
            String[] authorityStrArr = suggestedAuthority.split(",");
            authorityIntArr = new int[authorityStrArr.length];
            int i = 0;
            for(String str: authorityStrArr){
                authorityIntArr[i++] = Integer.parseInt(str.trim());
            }
        }
        
        //if a suggestion exists for this block already, remove it
        for(int j = 0; j < suggestions.size(); j++){
            if(suggestions.get(j).blockId == blockID){
                suggestions.remove(j);
                break;
            }
        }
        suggestions.add(new Suggestion(blockID, (int) suggestedSpeed, authorityIntArr));
        
        return;
    }
    public static void sendSuggestions(){
        //System.out.println(""+suggestions.size());
        if(suggestions.size() != 0){
            WaysideController.suggest(suggestions.toArray(new Suggestion[suggestions.size()]));
        }
    }
    public static CTCTrainData getTrainData(int blockID){
        for (CTCTrainData data: trainData){
            if(data.getBlockID() == blockID){
                return data;
            }
        }
        return null;
    }
    public static CTCTrainData getTrainDataTrainId(int trainID){
        for (CTCTrainData data: trainData){
            if(data.getTrainID() == trainID){
                return data;
            }
        }
        return null;
    }
    public static ArrayList<CTCTrainData> getAllTrainData(){
        return trainData;
    }
    public static void update(){
        int current_time = Environment.clock;
        
        //process any click events on the graph
        CTCGUI.handleGraphEvents();
        
        //add a train if the user entered one during the last update
        addTrain();
        //edit a train if the user changed one during the last update
        editTrain();
        
        updateTrack();
        
        //check that train hasn't left its block
        
        sendSuggestions();
        
        last_clock = current_time;
        return;
    }
    public static void updateTrack(){
        ArrayList<Integer> allHistory = new ArrayList<Integer>();
        for(int i = 0; i < trainData.size(); i++){
            for(int j = 0; j < trainData.get(i).historySize(); j++){
                allHistory.add(trainData.get(i).historyGet(j));
            }
        }
        Iterator itr = CTCGUI.getGraph().getEdgeIterator();
        while(itr.hasNext()) {
            Edge element = (Edge) itr.next();
            int blockId = Integer.parseInt(element.getId());
            boolean occupied = WaysideController.isOccupied(blockId);
            element.setAttribute("track.occupied", new Boolean(occupied));
            boolean classSet = false;
            if(occupied){
                CTCTrainData data = null;
                boolean inHistory = false;
                for(int i = 0; i < trainData.size(); i++){//these for loops will be expensive if there are a lot of trains but that probably won't be an issue
                    if(blockId == trainData.get(i).getBlockID()){
                        data = trainData.get(i);
                        break;
                    }
                    if(allHistory.contains(blockId)){
                        inHistory = true;
                        element.removeAttribute("ui.label");
                        classSet = true;
                        break;
                    }
                }
                //System.out.println("inHistory "+inHistory);
                //System.out.println("data null? "+(data==null));
                if(data == null && !inHistory){
                    //was unoccupied before, update traindata
                    //find neighbors
                    //System.out.println("here");
                    //classSet = false;
                    ArrayList<Edge> neighList = new ArrayList<Edge>();
                    Edge e = CTCGUI.getGraph().getEdge(""+blockId);
                    Iterator edgeIter = e.getNode0().getEdgeIterator();
                    while(edgeIter.hasNext()){
                        Edge ee = (Edge) edgeIter.next();
                        if(!ee.equals(e)){
                            neighList.add(ee);
                            //System.out.println("** "+ee.getId());
                        }
                    }
                    edgeIter = e.getNode1().getEdgeIterator();
                    while(edgeIter.hasNext()){
                        Edge ee = (Edge) edgeIter.next();
                        if(!ee.equals(e)){
                            neighList.add(ee);
                            //System.out.println("** "+ee.getId());
                        }
                    }
                    
                    //check if any neighbor was occupied
                    String edgestr = "";
                    int numOccEdges = 0;
                    CTCTrainData data2 = null;
                    CTCTrainData dataTemp = null;
                    for(int i = 0; i < neighList.size(); i++){
                        for(int j = 0; j < trainData.size(); j++){
                            //System.out.println("^^"+neighList.get(i).getId()+" "+trainData.get(j).getBlockID());
                            if(Integer.parseInt(neighList.get(i).getId()) == trainData.get(j).getBlockID()){
                                data2 = trainData.get(j);
                                dataTemp = data2;
                                break;
                            }
                        }
                        if(dataTemp != null){
                            numOccEdges++;
                            edgestr += neighList.get(i).getId()+", ";
                            dataTemp = null;
                        }
                    }
                    //System.out.println("-->numOccEdges"+numOccEdges);
                    if(numOccEdges > 1){
                        //if more than 1
                        throw new RuntimeException("Edges "+edgestr+
                            "were occupied. Then a train entered block "+element.getId()+
                            ". Train data could not be updated.");
                    }else if(numOccEdges == 1){
                        //if 1 then update traindata and suggestion
                        //change blockid, change authority
                        int oldBlockId = data2.getBlockID();
                        String newAuth = "";
                        //System.out.println(")))"+data2.getAuthority());
                        if(data2.getAuthority().length() != 0){
                            String[] authArr = data2.getAuthority().split(",");
                            for(int i = 0; i < authArr.length; i++){
                                if(Integer.parseInt(authArr[i]) != oldBlockId){
                                    if(newAuth.length() != 0){
                                        newAuth += ","+authArr[i];
                                    }else{
                                        newAuth = authArr[i];
                                    }
                                }
                            }
                        }
                        //System.out.println(")))"+newAuth);
                        data2.setAuthority(newAuth);
                        addSuggestion(data2.getTrainID(),data2.getSpeed(),newAuth);
                        //ensure the suggestion block changes
                        for(int i = 0; i < suggestions.size(); i++){
                            Suggestion oldSugg = suggestions.get(i);
                            if(oldSugg.blockId == oldBlockId){
                                Suggestion newSugg = new Suggestion(blockId, oldSugg.speed, oldSugg.authority);
                                suggestions.set(i,newSugg);
                                break;
                            }
                        }
                        data2.setBlockID(blockId);
                        data2.historyAdd(oldBlockId);
                        data2.setLastVisited(oldBlockId);
                        allHistory.add(oldBlockId);
                    }else{
                        //else mark as broken
                        classSet = true;
                        element.setAttribute("ui.class", "broken");
                    }
                }
                if(!classSet){
                    element.setAttribute("ui.class", "occupied");
                    element.addAttribute("ui.label",element.getId()+"");
                }
                //System.out.println("**CTC found block "+blockId+" occupied**");
            }else{
                //returned unoccupied
                if(allHistory.contains(blockId)){
                    allHistory.remove(new Integer(blockId));
                    for(int i = 0; i < trainData.size(); i++){
                        for(int j = 0; j < trainData.get(i).historySize(); j++){
                            if(trainData.get(i).historyGet(j) == blockId){
                                trainData.get(i).historyRemove(j);
                                break;
                            }
                        }
                    }
                }
                element.removeAttribute("ui.class");
                element.removeAttribute("ui.label");
                
            }
            //Boolean isSwitch = (Boolean) element.getAttribute("track.isSwitch");
            if(((Boolean) element.getAttribute("track.isSwitch")).booleanValue()){
                boolean switchState = WaysideController.getSwitch(blockId);
                element.setAttribute("track.switch", new Boolean(switchState));
                boolean signalState = WaysideController.getSignal(blockId);
                element.setAttribute("track.signal", new Boolean(signalState));
            }
            if(((Boolean) element.getAttribute("track.isCrossing")).booleanValue()){
                boolean xingState = WaysideController.getCrossing(blockId);
                element.setAttribute("track.crossing", new Boolean(xingState));
            }
        }
    }
}