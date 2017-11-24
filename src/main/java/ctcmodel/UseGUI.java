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

public class UseGUI {
    public static void main(String args[]){
        //TrackModel trackModel = new TrackModel();
        //TrackModel.init();
        
        //TrackModel.initWithTestData();
        //WaysideController.init();
        //TrainTracker trainTracker = new TrainTracker();
        CTCModel.init();
        CTCGUI.run();
    }
}