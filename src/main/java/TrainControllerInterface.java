// package traincontroller;

/**
 * This interface defines the inputs and outputs of the Train Controller module for the TMRC.
 *
 * @author Aric Hudson
 */
public interface TrainControllerInterface {
    
    /**
     * Computes the necessary power at which to set the train, given an authority.
     * 
     * @return is the power to which the train should be set.  If negative,
     * represents number of seconds brake should be applied.
     * 
     * NOTE: Could also simply return void and update the train.
     */
    double getPower();
}
