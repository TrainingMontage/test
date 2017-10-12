package utils;

import java.util.List;

public class Suggestion {
    private final int speed;
    private final List<Integer> authority;
    public Suggestion(int s, List<Integer> a) {
        speed = s;
        authority = a;
    }
}