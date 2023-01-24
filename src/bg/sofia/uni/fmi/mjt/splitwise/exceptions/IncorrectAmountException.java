package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class IncorrectAmountException extends CommandException {
    public IncorrectAmountException(String message) {
        super(message);
    }

    public IncorrectAmountException() {
        super("Incorrect amount specified");
    }
}
