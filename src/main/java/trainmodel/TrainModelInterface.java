package trainmodel;

public interface TrainModelInterface {
    public static int createTrain(int startingBlockId);
    public double getVelocity();
    public double getSuggestedSpeed();
    public double getAuthority();
    public double getMaxPower();
}