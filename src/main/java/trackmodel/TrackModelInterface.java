package trackmodel;

/**
 * StaticBlock is a class, just getters for statically visible stuff about track.
 * Authority and BlockStatus are enums.
 */
public interface TrackModelInterface {
    public static int getTrainAuthority(int trainId);
    public static double getTrainSpeed(int trainId);
    public static boolean setSignal(int blockId, boolean value);
    public static boolean getSignal(int blockId);
    public static boolean setSwitch(int blockId, boolean value);
    public static boolean getSwitch(int blockId);
    public static Authority setAuthority(int blockId, Authority auth);
    public static int setSpeed(int blockId, int speed);
    public static boolean setCrossing(int blockId, boolean active);
    public static boolean getCrossing(int blockId);
    public static byte[] getTrainBeacon(int trainId);
    public static int getPassengers(int trainId);
    public static boolean isIcyTrack(int trainId);
    public static StaticBlock getStaticBlock(int blockId);
    public static double getGrade(int trainId);
    public static boolean isOccupied(int blockId);
    public static BlockStatus getStatus(int blockId);
}