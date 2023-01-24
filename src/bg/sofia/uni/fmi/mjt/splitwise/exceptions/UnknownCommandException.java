package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class UnknownCommandException extends CommandException {
    public UnknownCommandException() {
        this("Illegal command. Check -h for available commands");
    }

    public UnknownCommandException(String message) {
        super(message);
    }
}
