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
 * @author Parth Dadhania
 */

package trainmodel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import trackmodel.TrackModel;
import traincontroller.TrainController;
import shared.Convert;
import shared.Environment;

public class TrainTracker{
    private static TrainTracker tracker;
    private static ArrayList<Train> trainList;
    //private static Train[] trainList;
    private static int trainId;

    public static TrainTracker getTrainTracker(){
        if(tracker == null){
            tracker = new TrainTracker();
            trainList = new ArrayList<Train>();
            trainId = 0;
        }
        return tracker;
    }

    public int createTrain(int blockId){
        int temp = trainId;
        Train newTrain = new Train(temp, blockId);
        trainList.add(newTrain);
        //trainList[temp] = newTrain;
        trainId = trainId+1;
        return temp;
    }
    public int createTrainTest(int blockId){
        int temp = trainId;
        TrackModel tm;
        tm = TrackModel.getTrackModel();
        Train newTrain = new Train(temp, blockId, tm,1);
        trainList.add(newTrain);
        //trainList[temp] = newTrain;
        trainId = trainId+1;
        return temp;
    }
    public Train getTrain(int retrieveTrain){
        //Train trainModel = trainList[retrieveTrain];
        Train trainModel = trainList.get(retrieveTrain);
        return trainModel;
    }
    /*
    public void removeTrain(int removeTrainId){
        removeTrainId = removeTrainId - 1;
        trainList.remove(removeTrainId);
    }*/

    public int getSize(){
        return trainList.size();
    }

    public static void main(String args[]){
        TrainModelGUI gui = new TrainModelGUI();
        gui.setVisible(true);
    }
}
