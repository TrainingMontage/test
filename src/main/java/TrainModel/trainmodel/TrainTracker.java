package trainmodel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.lang.Math;
import java.*;
import java.util.*;

/**
 *
 * @author Parth
 */
public class TrainTracker{
    ArrayList<Train> trainList = new ArrayList<Train>();
    int trainId = 0;
    int blockId = 0;
    public void createTrain(){
        Train newTrain = new Train(trainId, blockId);
        trainList.add(newTrain);
        trainId++;
    }
    public static void main(String args[]){
        TrainModelGUI gui = new TrainModelGUI();
        gui.setVisible(true);
    }
}
