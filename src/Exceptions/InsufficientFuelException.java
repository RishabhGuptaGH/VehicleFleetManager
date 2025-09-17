package Exceptions;

public class InsufficientFuelException extends Exception {
    //For insufficient fuel in move().
    public InsufficientFuelException(String message) {
        super(message);
    }
}
