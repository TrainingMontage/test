package trackmodel;

public interface TrackModelInterface {
    public static int getTrainAuthority(int trainId);
    public static double getTrainSpeed(int trainId);
    public static boolean setSignal(int blockId, boolean value);
    public static boolean getSignal(int blockId);
    public static boolean setSwitch(int blockId, boolean value);
    public static boolean getSwitch(int blockId);
    public static Authority setAuthority(int blockId, Authority auth);
    public static int setSpeed(int blockId, int speed);
    public static byte[] getTrainBeacon(int trainId);
}