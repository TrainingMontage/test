package shared;

public class CrashIntoSwitchException extends RuntimeException {
    // Parameterless Constructor
    public CrashIntoSwitchException() {}

    // Constructor that accepts a message
    public CrashIntoSwitchException(String message) {
        super(message);
    }
}