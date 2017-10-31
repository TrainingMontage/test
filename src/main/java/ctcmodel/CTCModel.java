package CTCModel;

import java.util.ArrayList;
import shared.BlockStatus;
import shared.Suggestion;

public class CTCModel{
    private static CTCModel model = null;
    private static TrainModel trainModel;
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
    
    private CTCModel(TrainModel atrainModel){
        trainModel = atrainModel;
    }
    
    public static void init(TrainModel atrainModel){
        if (model == null) {
            model = new CTCModel(atrainModel);
        }
    }
    
    static int checkTrainInputs(String block, String speed, String authority, String destination){
        int tempint;
        //block number test
        try{
            tempint = Integer.parseInt(block);
        }catch(NumberFormatException ex){
            return 1;
        }
        if(tempint != 0){//have to spawn next to the yard TODO: make this not fixed ID
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
        if(tempint < 0 || tempint > 5){
            return 3;
        }
        //destination test
        try{
            tempint = Integer.parseInt(destination);
        }catch(NumberFormatException ex){
            return 4;
        }
        if(tempint < 0 || tempint > 3){
            return 4;
        }
        return 0;
    }
    
    public static int createTrain(int startingBlockID, int suggestedSpeed,
                                  String suggestedAuth, int destBlockID){
        int trainID = trainModel.createTrain(startingBlockID);
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
        WaysideController.suggest(suggestions.toArray(new Suggestion[suggestions.size()]));
    }
    public static CTCTrainData getTrainData(int blockID){
        for (CTCTrainData data: trainData){
            if(data.getBlockID() == blockID){
                return data;
            }
        }
        return null;
    }
    //TODO: i was writing a new function here but i forgot what it was. i'm leaving this as a reminder for now
    //public static int
    
}