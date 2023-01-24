package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class UserDoesnotExistException extends CommandException {
    public UserDoesnotExistException(String username) {
        super("User with username %s doesn't exist".formatted(username));
    }
}
