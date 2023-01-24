package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class IncorrectCredentialsException extends CommandException {
    public IncorrectCredentialsException() {
        super("Incorrect credentials.");
    }
}
