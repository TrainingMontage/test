package shared;

public class TrainCrashException extends RuntimeException {
    // Parameterless Constructor
    public TrainCrashException() {}

    // Constructor that accepts a message
    public TrainCrashException(String message) {
        super(message);
    }
}