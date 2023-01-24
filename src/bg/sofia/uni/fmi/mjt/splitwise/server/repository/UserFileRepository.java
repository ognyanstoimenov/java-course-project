package bg.sofia.uni.fmi.mjt.splitwise.server.repository;

import bg.sofia.uni.fmi.mjt.splitwise.models.User;

import java.nio.file.Path;
import java.util.List;

public class UserFileRepository extends Repository<User> {

    private final Path usersPath;

    public UserFileRepository(String usersPath) {
        super(User[].class);
        this.usersPath = Path.of(usersPath);
    }

    public List<User> getUsers() {
        return getFromPath(usersPath);
    }

    public void replaceAll(List<User> users) {
        replaceInPath(usersPath, users);
    }

}
