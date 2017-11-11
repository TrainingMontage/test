package trainmodel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.lang.Math;
import java.*;

/**
 *
 * @author Parth
 */
public class TrainTracker{
    ArrayList trainList = new ArrayList();
    int trainId = 0;
    int blockId = 0;
    public void createTrain(){
        Train newTrain = new Train(trainId, blockId)
        trainId++;
    }
}
