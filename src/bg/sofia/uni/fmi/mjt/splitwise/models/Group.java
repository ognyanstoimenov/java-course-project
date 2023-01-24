package bg.sofia.uni.fmi.mjt.splitwise.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Group {
    private final UUID id;

    private final String name;

    private final Set<String> users;

    private final List<Transaction> transactions = new ArrayList<>();

    public Group(String user1, String user2) {
        this(null, Set.of(user1, user2));
    }

    public Group(String name, Set<String> users) {
        this.id = UUID.randomUUID();
        this.users = users;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public Set<String> getUsers() {
        return users;
    }

    public String getName() {
        return name;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public boolean isFriendGroup() {
        return users.size() == 2;
    }

    public String getOtherUser(String firstUser) {
        List<String> list = users.stream().filter(x -> !x.equals(firstUser)).toList();

        if (list.size() != 1) {
            throw new IllegalArgumentException("This is not a friendship group");
        }
        return list.get(0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof final Group other)) {
            return false;
        }

        return id == other.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
