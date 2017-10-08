package trainmodel;

public interface TrainModelInterface {
    public static int createTrain(int startingBlockId);
    public double getVelocity();
    public double getDisplacement();
    public double getSuggestedSpeed();
    public double getAuthority();
    public double getMaxPower();
    public int boardPassengers();
    public int offBoardPassengers();
    public boolean getLights();
    public boolean setLights(boolean turnOn);
    public boolean getLeftDoors();
    public boolean setLeftDoors(boolean open);
    public boolean getRightDoors();
    public boolean setRightDoors(boolean open);
}