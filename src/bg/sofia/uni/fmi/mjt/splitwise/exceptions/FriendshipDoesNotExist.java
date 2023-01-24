package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class FriendshipDoesNotExist extends CommandException{

    public FriendshipDoesNotExist(String username) {
        super("User %s is not your friend".formatted(username));
    }
}
