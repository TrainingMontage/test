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

public class TrainTracker{
    ArrayList<Train> trainList = new ArrayList<Train>();
    int trainId = 0;
    public void createTrain(int blockId){
        Train newTrain = new Train(trainId, blockId);
        trainList.add(newTrain);
        trainId++;
    }
    public static void main(String args[]){
        TrainModelGUI gui = new TrainModelGUI();
        gui.setVisible(true);
    }
}
