package Exceptions;

public class InvalidOperationException extends Exception {
    //For negative distances, invalid unloads, etc.
    public InvalidOperationException(String message) {
        super(message);
    }
}
