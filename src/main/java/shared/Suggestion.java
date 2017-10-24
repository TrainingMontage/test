package shared;

import java.util.List;

public class Suggestion {
    public final int blockId;
    public final int speed;
    public final int[] authority;
    public Suggestion(int b, int s, int[] a) {
        blockId = b;
        speed = s;
        authority = a;
    }
}