package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class SessionException extends CommandException {
    public SessionException(boolean shouldBeLoggedIn) {
        super("User should be logged %s".formatted(shouldBeLoggedIn ? "in":"out"));
    }
}
