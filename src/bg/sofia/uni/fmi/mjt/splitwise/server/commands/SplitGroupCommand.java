package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.GroupDoesNotExistException;
import bg.sofia.uni.fmi.mjt.splitwise.models.Group;
import bg.sofia.uni.fmi.mjt.splitwise.models.Transaction;
import bg.sofia.uni.fmi.mjt.splitwise.server.Session;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.GroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.UserRepository;

import java.util.List;
//$ split-group <amount> <group_name> <reason_for_payment>
public class SplitGroupCommand extends GroupCommand {
    private static final String SUCCESS_RESPONSE = "Successfully split in group!";

    private static final int AMOUNT_INDEX = 0;
    private static final int GROUP_NAME_INDEX = 1;
    private static final int COMMENT_INDEX = 2;


    public SplitGroupCommand(UserRepository userRepository, GroupRepository groupRepository) {
        super(userRepository, groupRepository, 3, 3);
    }

    @Override
    public String run(List<String> arguments, Session session) throws CommandException {
        validateArguments(arguments, session, true);

        String currentUser = session.getCurrentUser();

        float amount = Float.parseFloat(arguments.get(AMOUNT_INDEX));
        String groupName = arguments.get(GROUP_NAME_INDEX);
        String comment = arguments.get(COMMENT_INDEX);

        Group group = groupRepository.getGroupOfUserByName(groupName, currentUser);

        if (group == null) {
            throw new GroupDoesNotExistException();
        }

        Transaction transaction = new Transaction(currentUser, amount, comment);
        group.addTransaction(transaction);

        groupRepository.updateGroup(group);

        return SUCCESS_RESPONSE;
    }
}
