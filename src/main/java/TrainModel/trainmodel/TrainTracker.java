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
    //private static ArrayList<Train> trainList;
    private static Train[] trainList;
    private static int trainId;

    /**
     * Train Tracker singalton
     * @return Train Tracker object
     */
    public static TrainTracker getTrainTracker(){
        if(tracker == null){
            tracker = new TrainTracker();
            //trainList = new ArrayList<Train>();
            trainList = new Train[100];
            trainId = 0;
        }
        return tracker;
    }

    /**
     * CTC creates a new train
     * @param  blockId  Starting block id
     * @return Train id
     */
    public int createTrain(int blockId){
        int temp = trainId;
        Train newTrain = new Train(temp, blockId);
        //trainList.add(newTrain);
        trainList[temp] = newTrain;
        int i;
        String[] guiList = new String[this.getSize()];
        for(i = 0; i<100; i++){
            if(trainList[i] != null){
                Train addId = trainList[i];
                String s = Integer.toString(addId.getTrainId());
                guiList[i] = "Train " + s;
            }
        }
        for(i = 0; i<100; i++){
            if(trainList[i] != null){
                Train addId = trainList[i];
                addId.getGUI().trainIdPicker.setModel(new javax.swing.DefaultComboBoxModel<>(guiList));
            }
        }
        newTrain.getGUI().trainIdPicker.setSelectedIndex(temp);
        trainId = trainId+1;
        return temp;
    }

    /**
     * JUnit testing method for createTrain
     * @param  blockId Starting block id
     * @return Train id
     */
    public int createTrainTest(int blockId){
        int temp = trainId;
        TrackModel tm;
        tm = TrackModel.getTrackModel();
        Train newTrain = new Train(temp, blockId, tm,1);
        //trainList.add(newTrain);
        trainList[temp] = newTrain;
        trainId = trainId+1;
        return temp;
    }

    /**
     * Gets the train object specified
     * @param  retrieveTrain train id of the train to get
     * @return A train model object
     */
    public Train getTrain(int retrieveTrain){
        Train trainModel = trainList[retrieveTrain];
        //Train trainModel = trainList.get(retrieveTrain);
        return trainModel;
    }

    /**
     * getTrain test method without GUI
     * @param retrieveTrain id of train to get
     * @return
     */
    public Train getTrainTest(int retrieveTrain){
        if(trainList[retrieveTrain] != null){
            Train trainModel = trainList[retrieveTrain];
            return trainModel;
        }
        else{
            return null;
        }
    }
    /**
     * Remove train
     * @param removeTrainId id of train to be removed
     */
    public void removeTrain(int removeTrainId){
        //removeTrainId = removeTrainId - 1;
        //trainList.remove(removeTrainId);
        trainList[removeTrainId] = null;
    }

    /**
     * Size of the arrayList of Trains
     * @return the size of the train arrayList
     */
    public int getSize(){
        int i = 0;
        int num = 0;
        while(i < trainList.length){
            if(trainList[i] != null){
                num++;
            }
            i++;
        }
        return num;
    }

    public static void main(String args[]){
        if(TrainTracker.getTrainTracker().getTrain(0) != null){

        }
        TrainModelGUI gui = new TrainModelGUI();
        gui.setVisible(true);
    }
}
