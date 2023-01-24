package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class UserExistsException extends CommandException {

    public UserExistsException() {
        super("User with same username exists.");
    }

}
