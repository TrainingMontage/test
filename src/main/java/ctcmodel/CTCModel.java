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
import java.util.HashMap;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import javax.swing.JOptionPane;
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
    private static ArrayList<Suggestion> suggestions;
    private static ArrayList<CTCTrainData> trainData;
    private static int last_clock;
    private static int current_time;
    private static StaticTrack track;
    private static ArrayList<Integer> bIds;
    private static HashMap<String,Integer> sch2TIdMap;
    private static HashMap<String,Integer> schDestNum;
    private static HashMap<String,String> schLines;
    private static HashMap<String,ArrayList<Integer>> schTimes;
    private static HashMap<String,ArrayList<Integer>> schDests;
    private static File schFile;
    private static String fullFile;
    
    //Train-To-Add (TTA) data (to ensure trains aren't added in the middle of an update)
    private static ArrayList<Integer> TTAstartingBlock;
    private static ArrayList<Double> TTAspeed;
    private static ArrayList<String> TTAauthority;
    private static ArrayList<Integer> TTAdestBlock;
    
    //Train-To-Edit (TTE) data (to ensure trains aren't edited in the middle of an update)
    private static ArrayList<Integer> TTEtrainID;
    private static ArrayList<Double> TTEspeed;
    private static ArrayList<String> TTEauthority;
    private static ArrayList<Integer> TTEdestBlock;
    
    private CTCModel(){
        //trainTracker = atrainTracker;
        last_clock = 0;
        suggestions = new ArrayList<Suggestion>();
        trainData = new ArrayList<CTCTrainData>();
        TTAstartingBlock = new ArrayList<Integer>();
        TTAspeed = new ArrayList<Double>();
        TTAauthority = new ArrayList<String>();
        TTAdestBlock = new ArrayList<Integer>();
        TTEtrainID = new ArrayList<Integer>();
        TTEspeed = new ArrayList<Double>();
        TTEauthority = new ArrayList<String>();
        TTEdestBlock = new ArrayList<Integer>();
        sch2TIdMap = new HashMap<String,Integer>();
        schDestNum = new HashMap<String,Integer>();
        schLines = new HashMap<String,String>();
        schTimes = new HashMap<String,ArrayList<Integer>>();
        schDests = new HashMap<String,ArrayList<Integer>>();
        schFile = null;
        fullFile = null;
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
        if(line != 152 && line != 153){//have to spawn next to the yard TODO: make this not fixed ID
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
        TTAauthority.add(suggestedAuth);
        TTAstartingBlock.add(startingBlockID);
        TTAspeed.add(suggestedSpeed);
        TTAdestBlock.add(destBlockID);
    }
    public static void editTrain(int trainID, double suggestedSpeed,
                                 String suggestedAuth, int destBlockID){
        TTEtrainID.add(trainID);
        TTEauthority.add(suggestedAuth);
        TTEspeed.add(suggestedSpeed);
        TTEdestBlock.add(destBlockID);
    }
    private static void addTrain(){
        while(TTAauthority.size() != 0){
            int trainID = TrainTracker.getTrainTracker().createTrain(TTAstartingBlock.get(0));
            trainData.add(new CTCTrainData(trainID, TTAstartingBlock.get(0), TTAspeed.get(0),
                                           TTAauthority.get(0), TTAstartingBlock.get(0), TTAdestBlock.get(0)));
            addSuggestion(trainID, TTAspeed.get(0), TTAauthority.get(0));
            TTAspeed.remove(0);
            TTAdestBlock.remove(0);
            TTAstartingBlock.remove(0);
            TTAauthority.remove(0);
        }
    }
    protected static void editTrain(){
        while(TTEauthority.size() != 0){
            //edit ctc train data
            int trainId = TTEtrainID.get(0);
            for (CTCTrainData data: trainData){
                if(data.getTrainID() == trainId){
                    data.setSpeed(TTEspeed.get(0));
                    data.setAuthority(TTEauthority.get(0));
                    data.setDestination(TTEdestBlock.get(0));
                    break;
                }
            }
            //edit suggestion
            addSuggestion(trainId, TTEspeed.get(0), TTEauthority.get(0));
            TTEspeed.remove(0);
            TTEdestBlock.remove(0);
            TTEtrainID.remove(0);
            TTEauthority.remove(0);
        }
    }
    public static void addSuggestion(int trainID, double suggestedSpeed, String suggestedAuthority){
        //there are no checks for input correctness here because checkTrainInputs must be called before using this function
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
    public static void readSchedule(File file){
        schFile = file;
    }
    public static void readSchedule(){
        //each line is expected to be the following:
        //    <unique train identifier>,<line ("RED" or "GREEN")>,<departure time (minutes after 8:00)>,<destination block id>
        if(schFile != null){
            fullFile = "";
            sch2TIdMap.clear();
            schDestNum.clear();
            schTimes.clear();
            schDests.clear();
            schLines.clear();
            try{
                BufferedReader br = new BufferedReader(new FileReader(schFile));
                String line;
                int linenum = 1;
                int blockId;
                while ( (line = br.readLine()) != null) {
                    fullFile = fullFile+line+"\n";
                    String[] values = line.split(",");
                    if(values.length != 4){
                        JOptionPane.showMessageDialog(null, "Wrong number of fields in schedule file on line "+linenum, "Error reading schedule", JOptionPane.ERROR_MESSAGE);
                        sch2TIdMap.clear();
                        schDestNum.clear();
                        schTimes.clear();
                        schDests.clear();
                        schLines.clear();
                        br.close();
                        schFile = null;
                        return;
                        //throw new Exception("Wrong number of fields in schedule file on line "+linenum);
                    }
                    for(int i = 0; i < values.length; i++){
                        values[i] = values[i].trim();
                    }
                    if(!sch2TIdMap.containsKey(values[0])){
                        sch2TIdMap.put(values[0],null);
                        schTimes.put(values[0],new ArrayList<Integer>());
                        schDests.put(values[0],new ArrayList<Integer>());
                        schDestNum.put(values[0],-1);
                        if(!values[1].equals("GREEN") && !values[1].equals("RED")){
                            JOptionPane.showMessageDialog(null, "Invalid line color in schedule file on line "+linenum, "Error reading schedule", JOptionPane.ERROR_MESSAGE);
                            sch2TIdMap.clear();
                            schDestNum.clear();
                            schTimes.clear();
                            schDests.clear();
                            schLines.clear();
                            br.close();
                            schFile = null;
                            return;
                            //throw new Exception("Invalid line color in schedule file on line "+linenum);
                        }
                        schLines.put(values[0],values[1]);
                    }
                    schTimes.get(values[0]).add(Integer.parseInt(values[2]));
                    blockId = Integer.parseInt(values[3]);
                    if(!track.getStaticBlock(blockId).getLine().equals(schLines.get(values[0]))){
                        JOptionPane.showMessageDialog(null, "Block Id on line "+linenum+" does not exist on line "+schLines.get(values[0]), "Error reading schedule", JOptionPane.ERROR_MESSAGE);
                        sch2TIdMap.clear();
                        schDestNum.clear();
                        schTimes.clear();
                        schDests.clear();
                        schLines.clear();
                        br.close();
                        schFile = null;
                        return;
                        //throw new Exception("Block Id on line "+linenum+" does not exist on line "+schLines.get(values[0]));
                    }
                    schDests.get(values[0]).add(blockId);
                    
                    linenum++;
                }
                CTCGUI.setScheduleText(fullFile);
                //System.out.println("after read");
                //System.out.println(fullFile);
                br.close();
            }catch(Exception e){
                throw new RuntimeException(e);
            }
            schFile = null;
        }
    }
    private static void scheduleTrains(){
        //check if the any train wants to depart at this time
        //for each found
        //check that it is at its prev destination
        //if yes, change dest
        //if no, do nothing
        
        //check index = schDestNum+1
        
        int stopNumber;
        boolean createdTrainThisUpdateGreen = false;//ensure you don't spawn a train twice on the same block
        boolean createdTrainThisUpdateRed = false;
        boolean trainSpawnedThisUpdate = false;
        //for each scheduled train
        for(String schId : sch2TIdMap.keySet()){
            CTCTrainData data;
            if(sch2TIdMap.get(schId) == null){
                data = null;
            }else{
                data = getTrainDataTrainId(sch2TIdMap.get(schId));
            }
            //check if this train wants to depart
            stopNumber = schDestNum.get(schId)+1;
            if(stopNumber >= schTimes.get(schId).size()){
                //the current destination is the last scheduled stop
                continue;
            }
            if(schTimes.get(schId).get(stopNumber)*60 < current_time){
                //if data==null then this train hasn't been created
                if(data == null){
                    //if block is unoccupied
                    int startBlock;
                    if(schLines.get(schId).equals("GREEN")){
                        startBlock = 152;
                        trainSpawnedThisUpdate = createdTrainThisUpdateGreen;
                    }else if(schLines.get(schId).equals("RED")){
                        startBlock = 153;
                        trainSpawnedThisUpdate = createdTrainThisUpdateRed;
                    }else{
                        startBlock = -1;
                    }
                    Edge e = CTCGUI.getGraph().getEdge(""+startBlock);
                    if(!((Boolean)e.getAttribute("track.occupied")) && !trainSpawnedThisUpdate){
                        if(startBlock == 152){
                            createdTrainThisUpdateGreen = true;
                        }else if(startBlock == 153){
                            createdTrainThisUpdateRed = true;
                        }
                        //update destNum
                        schDestNum.put(schId,schDestNum.get(schId)+1);
                        //call createTrain
                        //int tId = createTrain(startBlock, 0, "", schDests.get(schId).get(schDestNum.get(schId)));
                        int trainID = TrainTracker.getTrainTracker().createTrain(startBlock);
                        trainData.add(new CTCTrainData(trainID, startBlock, 0, "", startBlock, schDests.get(schId).get(schDestNum.get(schId))));//let routing fill in speed and auth
                        addSuggestion(trainID, 0, "");
                        sch2TIdMap.put(schId,trainID);
                    }
                }else{
                    //check if this train is at its destination
                    if(data.getBlockID() == data.getDestination()){
                        //good to go to next stop
                        //update destNum
                        schDestNum.put(schId,schDestNum.get(schId)+1);
                        //call editTrain
                        editTrain(data.getTrainID(), data.getSpeed(), data.getAuthority(), schDests.get(schId).get(schDestNum.get(schId)));
                    }
                }
            }
        }
    }
    public static void update(){
        current_time = Environment.clock;
        
        if(fullFile != null){
            CTCGUI.setScheduleText(fullFile);
        }
        
        //if there is a new schedule file, read it
        readSchedule();
        scheduleTrains();
        
        //add a train if the user entered one during the last update
        addTrain();
        //edit a train if the user changed one during the last update
        editTrain();
        
        updateTrack();
        
        //process any click events (also any routing) on the graph
        //update track must be called before this so CTC internal data matches other module data
        CTCGUI.handleGraphEvents();
        
        //edit a train if it was changed during routing
        editTrain();
        
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
                if(data == null && !inHistory){
                    //was unoccupied before, update traindata
                    //find neighbors
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
                    String gLine = "GREEN";
                    if(gLine.equals((String)element.getAttribute("track.line"))){
                        element.setAttribute("ui.class", "occupied");
                    }else{
                        element.setAttribute("ui.class", "redoccupied");
                    }
                    element.addAttribute("ui.label",element.getId()+"");
                }
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
                String rLine = "RED";
                if(rLine.equals((String)element.getAttribute("track.line"))){
                    element.setAttribute("ui.class", "red");
                }
                element.removeAttribute("ui.label");
                
            }
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