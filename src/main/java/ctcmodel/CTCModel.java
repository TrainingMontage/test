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
    
    private CTCModel(){
        //trainTracker = atrainTracker;
        last_clock = 0;
        suggestions = new ArrayList<Suggestion>();
        trainData = new ArrayList<CTCTrainData>();
    }
    
    public static void init(){
        if (model == null) {
            model = new CTCModel();
        }
        track = TrackModel.getTrackModel().getStaticTrack();
        CTCGUI.init();
    }
    
    static int checkTrainInputs(String block, String speed, String authority, String destination){
        int tempint;
        //block number test
        try{
            tempint = Integer.parseInt(block);
        }catch(NumberFormatException ex){
            return 1;
        }
        if(tempint != 152){//have to spawn next to the yard TODO: make this not fixed ID
            return 1;
        }
        //speed test
        try{
            tempint = Integer.parseInt(speed);
        }catch(NumberFormatException ex){
            return 2;
        }
        //authority test
        try{
            for(String blockID: authority.split(",")){
                //TODO: add a stricter test? do those blockIDs exist? check for contiguous?
                tempint = Integer.parseInt(blockID.trim());
            }
        }catch(NumberFormatException ex){
            return 3;
        }
        //destination test
        try{
            tempint = Integer.parseInt(destination);
        }catch(NumberFormatException ex){
            return 4;
        }
        return 0;
    }
    
    public static int createTrain(int startingBlockID, int suggestedSpeed,
                                  String suggestedAuth, int destBlockID){
        int trainID = TrainTracker.getTrainTracker().createTrain(startingBlockID);
        trainData.add(new CTCTrainData(trainID, startingBlockID, suggestedSpeed,
                                       suggestedAuth, startingBlockID, destBlockID));
        return trainID;
    }
    public static void addSuggestion(int trainID, int suggestedSpeed, String suggestedAuthority){
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
        String[] authorityStrArr = suggestedAuthority.split(",");
        int[] authorityIntArr = new int[authorityStrArr.length];
        int i = 0;
        for(String str: authorityStrArr){
            authorityIntArr[i++] = Integer.parseInt(str.trim());
        }
        for(Suggestion sugg: suggestions){
            if(sugg.blockId == blockID){
                suggestions.remove(sugg);
            }
        }
        suggestions.add(new Suggestion(blockID, suggestedSpeed, authorityIntArr));
        
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
    public static void update(){
        int current_time = Environment.clock;
        
        updateTrack();
        
        sendSuggestions();
        
        last_clock = current_time;
        return;
    }
    public static void updateTrack(){
        Iterator itr = CTCGUI.getGraph().getEdgeIterator();
        while(itr.hasNext()) {
            Edge element = (Edge) itr.next();
            int blockId = Integer.parseInt(element.getId());
            boolean occupied = WaysideController.isOccupied(blockId);
            element.setAttribute("track.occupied", new Boolean(occupied));
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
    //TODO: i was writing a new function here but i forgot what it was. i'm leaving this as a reminder for now
    //public static int
    
}