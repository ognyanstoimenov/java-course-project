package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.GroupExistsException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserDoesnotExistException;
import bg.sofia.uni.fmi.mjt.splitwise.models.Group;
import bg.sofia.uni.fmi.mjt.splitwise.server.Session;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.GroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CreateGroupCommand extends GroupCommand {
    private static final String SUCCESS_RESPONSE = "Successfully created group";

    public CreateGroupCommand(UserRepository userRepository, GroupRepository groupRepository) {
        super(userRepository, groupRepository, 2, Integer.MAX_VALUE);
    }

    @Override
    public String run(List<String> arguments, Session session) throws CommandException {
        validateArguments(arguments, session, true);

        String currentUser = session.getCurrentUser();
        String groupName = arguments.get(0);
        // Remove duplicates
        Set<String> users = new HashSet<>(arguments.subList(1, arguments.size()));
        users.add(currentUser); // Current user should always be part of the group.

        if (groupRepository.doesGroupWithUsersExist(users)) {
            throw new GroupExistsException();
        }

        for (String username : users)  {
            if (!userRepository.doesUserExist(username)) {
                throw new UserDoesnotExistException(username);
            }
        }

        if (users.size() < 3) {
            throw new IllegalArgumentException("Users in a group should be at least 3");
        }

        groupRepository.updateGroup(new Group(groupName, users));

        return SUCCESS_RESPONSE;
    }
}
