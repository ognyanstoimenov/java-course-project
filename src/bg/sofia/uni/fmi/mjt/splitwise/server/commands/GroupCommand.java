package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.repository.GroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.UserRepository;

public abstract class GroupCommand extends AbstractCommand {

    protected final GroupRepository groupRepository;

    public GroupCommand(UserRepository userRepository, GroupRepository groupRepository, int minArguments, int maxArguments) {
        super(userRepository, minArguments, maxArguments);
        this.groupRepository = groupRepository;
    }
}
