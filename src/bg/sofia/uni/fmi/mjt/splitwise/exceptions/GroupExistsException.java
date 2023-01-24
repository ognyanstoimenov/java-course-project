package bg.sofia.uni.fmi.mjt.splitwise.exceptions;

public class GroupExistsException extends CommandException {
    public GroupExistsException() {
        super("Group with the same users already exists.");
    }

}
