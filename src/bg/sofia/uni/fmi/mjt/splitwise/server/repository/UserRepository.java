package bg.sofia.uni.fmi.mjt.splitwise.server.repository;

import bg.sofia.uni.fmi.mjt.splitwise.models.User;

import java.util.List;

public class UserRepository {
    private final UserFileRepository repository;

    public UserRepository(UserFileRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("Argument repository can't be null");
        }

        this.repository = repository;
    }

    public List<User> getUsers() {
        return repository.getUsers();
    }

    public boolean doesUserExist(String username, String password) {
        return !getUsers().stream()
                .filter(x -> x.getUsername().equals(username) && x.getPassword().equals(password))
                .toList().isEmpty();
    }

    public boolean doesUserExist(String username) {
        return getUser(username) != null;
    }

    private User getUser(String username) {
        return getUsers().stream()
                .filter(x -> x.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public void logInUser(String username) {
        List<User> users = getUsers();
        User loggedInUser = users.stream()
                .filter(x -> x.getUsername().equals(username))
                .findFirst()
                .orElse(null);
        assert loggedInUser != null;
        loggedInUser.logIn();
        replaceAll(users);
    }

    public void replaceAll(List<User> users) {
        repository.replaceAll(users);
    }

}
