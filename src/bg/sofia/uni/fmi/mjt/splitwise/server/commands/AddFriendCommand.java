package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.AlreadyFriendsException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserDoesnotExistException;
import bg.sofia.uni.fmi.mjt.splitwise.models.Group;
import bg.sofia.uni.fmi.mjt.splitwise.server.Session;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.GroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.UserRepository;

import java.util.List;
import java.util.Set;

public class AddFriendCommand extends GroupCommand {
    private static final String SUCCESS_RESPONSE = "Successfully added friend";

    public AddFriendCommand(UserRepository userRepository, GroupRepository groupRepository) {
        super(userRepository, groupRepository, 1, 1);
    }

    @Override
    public String run(List<String> arguments, Session session) throws CommandException {
        validateArguments(arguments, session, true);
        String currentUser = session.getCurrentUser();

        String friendUsername = arguments.get(0);
        Group friendship = new Group(currentUser, friendUsername);
        boolean exists = groupRepository.doesGroupWithUsersExist(Set.of(currentUser, friendUsername));
        if (exists) {
            throw new AlreadyFriendsException();
        }

        if (!userRepository.doesUserExist(friendUsername)) {
            throw new UserDoesnotExistException(friendUsername);
        }

        groupRepository.updateGroup(friendship);

        return SUCCESS_RESPONSE;
    }
}
