package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.FriendshipDoesNotExist;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserDoesnotExistException;
import bg.sofia.uni.fmi.mjt.splitwise.models.Group;
import bg.sofia.uni.fmi.mjt.splitwise.models.Transaction;
import bg.sofia.uni.fmi.mjt.splitwise.server.Session;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.GroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.UserRepository;

import java.util.List;

//$ split <amount> <username> <reason_for_payment>

public class SplitCommand extends GroupCommand {
    private static final String SUCCESS_RESPONSE = "Successfully split!";

    private static final int AMOUNT_INDEX = 0;
    private static final int USERNAME_INDEX = 1;
    private static final int COMMENT_INDEX = 2;

    public SplitCommand(UserRepository userRepository, GroupRepository groupRepository) {
        super(userRepository, groupRepository, 3, 3);
    }

    @Override
    public String run(List<String> arguments, Session session) throws CommandException {
        validateArguments(arguments, session, true);

        String currentUser = session.getCurrentUser();

        float amount = Float.parseFloat(arguments.get(AMOUNT_INDEX));
        String username = arguments.get(USERNAME_INDEX);
        String comment = arguments.get(COMMENT_INDEX);

        if (!userRepository.doesUserExist(username)) {
            throw new UserDoesnotExistException(username);
        }

        Group friendship = groupRepository.getFriendshipGroup(currentUser, username);
        if (friendship == null) {
            throw new FriendshipDoesNotExist(username);
        }

        Transaction transaction = new Transaction(currentUser, amount, comment);
        friendship.addTransaction(transaction);

        groupRepository.updateGroup(friendship);
        return SUCCESS_RESPONSE;
    }
}
