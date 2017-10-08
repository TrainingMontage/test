package wayside;

public interface WCInterface {
    public Block getAuthority(int blockId);
    public int getSpeed(int blockId);
    public static void suggest(int blockId, int finalBlock, int speed);
    public boolean occupiedBlock(int blockId);
    public boolean lightState(int blockId);
    public boolean switchState(int blockId);
    public boolean brokenBlock(int blockId);
    public boolean inMaintenenceBlock(int blockId);
}