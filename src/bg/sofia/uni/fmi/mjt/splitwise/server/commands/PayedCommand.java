package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.IncorrectAmountException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserDoesnotExistException;
import bg.sofia.uni.fmi.mjt.splitwise.models.Group;
import bg.sofia.uni.fmi.mjt.splitwise.models.Transaction;
import bg.sofia.uni.fmi.mjt.splitwise.server.Debt;
import bg.sofia.uni.fmi.mjt.splitwise.server.DebtCalculator;
import bg.sofia.uni.fmi.mjt.splitwise.server.Session;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.GroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.UserRepository;

import java.util.List;

// payed <amount> <username> [group_name]
public class PayedCommand extends GroupCommand{
    private static final String SUCCESS_RESPONSE = "Successfully registered payment.";

    private static final int GROUP_NAME_INDEX = 2;
    private static final int PAYER_INDEX = 1;
    private static final int AMOUNT_INDEX = 0;
    private static final int MAX_ARGUMENTS = 3;
    private static final int MIN_ARGUMENTS = 2;

    public PayedCommand(UserRepository userRepository, GroupRepository groupRepository) {
        super(userRepository, groupRepository, MIN_ARGUMENTS, MAX_ARGUMENTS);
    }

    @Override
    public String run(List<String> arguments, Session session) throws CommandException {
        if (arguments == null || session == null) {
            throw new IllegalArgumentException("Arguments and currentUser can't be null");
        }
        validateArguments(arguments, session, true);

        String currentUser = session.getCurrentUser();
        float amount = Float.parseFloat(arguments.get(AMOUNT_INDEX));
        String payer = arguments.get(PAYER_INDEX);

        if (!userRepository.doesUserExist(payer)) {
            throw new UserDoesnotExistException(payer);
        }

        Group group;
        if (arguments.size() == MAX_ARGUMENTS) {
            String groupName = arguments.get(GROUP_NAME_INDEX);
            group = groupRepository.getGroupOfUserByName(groupName, currentUser);
        }
        else {
            group = groupRepository.getFriendshipGroup(currentUser, payer);
        }

        Debt debt = DebtCalculator.calculateDebtOfUser(payer, group, currentUser);
        if (debt == null || debt.getOwedAmount() < amount) {
            throw new IncorrectAmountException("User ows less than specified amount");
        }

        Transaction transaction = new Transaction(payer, amount, null, currentUser);
        group.addTransaction(transaction);
        groupRepository.updateGroup(group);

        return SUCCESS_RESPONSE;
    }
}
