package wayside;

public interface WCInterface {
    public Block getAuthority(Block trainPosition);
    public int getSpeed(Block trainPosition);
    public void suggest(Block trainPosition, Block authority, int speed);
    public boolean occupiedBlock(Block block);
    public boolean lightState(Block lightPosition);
    public boolean switchState(Block switchPosition);
    public boolean brokenBlock(Block block);
    public boolean inMaintenenceBlock(Block block);
}