package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class WrongNumberOfArgumentsException extends CommandException {
    public WrongNumberOfArgumentsException(int given) {
        super("Incorrect number of arguments: got %d".formatted(given));
    }
}
