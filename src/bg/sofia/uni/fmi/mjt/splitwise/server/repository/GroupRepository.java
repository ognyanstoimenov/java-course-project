package bg.sofia.uni.fmi.mjt.splitwise.server.repository;

import bg.sofia.uni.fmi.mjt.splitwise.models.Group;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class GroupRepository {
    private final GroupFileRepository repository;

    public GroupRepository(GroupFileRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Argument repository can't be null");
        }

        this.repository = repository;
    }

    public List<Group> getGroups() {
        return repository.getGroups();
    }

    public List<Group> getGroupsWithUsers(List<String> users) {
        return getGroups().stream()
                .filter(x -> x.getUsers().containsAll(users))
                .sorted(Comparator.comparing(Group::getUsers, Comparator.comparingInt(Set::size)))
                .toList();
    }

    public Group getFriendshipGroup(String user1, String user2) {
        return getGroups().stream()
                .filter(x -> x.getUsers().equals(Set.of(user1, user2)))
                .findFirst()
                .orElse(null);
    }

    public Group getGroupOfUserByName(String groupName, String username) {
        return getGroups().stream()
                .filter(x -> x.getName() != null && x.getName().equals(groupName))
                .filter(x -> x.getUsers().contains(username))
                .findFirst()
                .orElse(null);
    }

    public boolean doesGroupWithUsersExist(Set<String> users) {
        return !getGroups().stream()
                .filter(x -> x.getUsers().equals(users))
                .toList().isEmpty();
    }

    public void updateGroup(Group group) {
        repository.updateGroup(group);
    }

}
