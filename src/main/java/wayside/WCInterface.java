package wayside;

/**
 * Suggestion is a class holding speed, authority (List<blockId>).
 * BlockStatus is an enum holding {OK, BROKEN, IN_MAINTENANCE}
 */
public interface WCInterface {
    public static boolean isOccupied(int blockId);
    public static boolean getSignal(int blockId);
    public static boolean getSwitch(int blockId);
    public static boolean getCrossing(int blockId);
    public static void suggest(List<Suggestion> suggestion);
    public static BlockStatus getStatus(int blockId);
}