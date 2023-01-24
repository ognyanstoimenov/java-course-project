package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class AlreadyFriendsException extends CommandException {
    public AlreadyFriendsException() {
        super("Users are already friends.");
    }
}
