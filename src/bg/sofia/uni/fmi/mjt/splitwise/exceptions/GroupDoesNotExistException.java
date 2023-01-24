package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class GroupDoesNotExistException extends CommandException {
    public GroupDoesNotExistException() {
        super("Group with this name doesn't exist.");
    }

}