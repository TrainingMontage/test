package trainmodel;

public interface TrainModelInterface {
    public static int createTrain(int startingBlockId);
    public double getVelocity();
    public void setPower(double power);
    public double getSuggestedSpeed();
    public double getAuthority();
    public double getMaxPower();
}