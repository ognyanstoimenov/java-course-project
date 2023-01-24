package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.server.Status;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.models.Group;
import bg.sofia.uni.fmi.mjt.splitwise.server.Debt;
import bg.sofia.uni.fmi.mjt.splitwise.server.DebtCalculator;
import bg.sofia.uni.fmi.mjt.splitwise.server.Session;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.GroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class GetStatusCommand extends GroupCommand {
    public GetStatusCommand(UserRepository userRepository, GroupRepository groupRepository) {
        super(userRepository, groupRepository, 0, 0);
    }

    @Override
    public String run(List<String> arguments, Session session) throws CommandException {
        validateArguments(arguments, session, true);

        String currentUser = session.getCurrentUser();

        List<Group> groups = groupRepository.getGroupsWithUsers(List.of(currentUser));

        Map<Group, Status> status = new TreeMap<>(Comparator.comparing(Group::getUsers,
                Comparator.comparingInt(Set::size)));

        for (Group group : groups) {
            Set<Debt> owes = DebtCalculator.calculateDebtOfUser(currentUser, group);
            Set<Debt> isOwed = DebtCalculator.calculateOwesToUser(currentUser, group);
            status.put(group, new Status(owes, isOwed));

        }

        return outputStatus(status, currentUser);
    }

    private String outputStatus(Map<Group, Status> statuses, String currentUser) {
        StringBuilder result = new StringBuilder();
        result.append("Friends:\n");
        boolean printingFriends = true;
        for (Map.Entry<Group, Status> groupStatus : statuses.entrySet()) {
            Group group = groupStatus.getKey();
            if (!group.isFriendGroup() && printingFriends) {
                result.append("Groups:\n");
                printingFriends = false;
            }

            String title = group.isFriendGroup() ? group.getOtherUser(currentUser) : group.getName();
            result.append(title).append(":").append("\n");

            Status status = groupStatus.getValue();

            status.isOwed().forEach(x -> {
                result.append("* %s: Owes you %f".formatted(x.getInDebtUser(), x.getOwedAmount()));
                result.append("\n");
            });

            status.owes().forEach(x -> {
                result.append("* %s: You owe them %f".formatted(x.getOwedAmount(), x.getOwedAmount()));
                result.append("\n");
            });
        }

        return result.toString();
    }
}
