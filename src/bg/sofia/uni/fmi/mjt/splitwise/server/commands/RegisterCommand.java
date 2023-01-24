package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserExistsException;
import bg.sofia.uni.fmi.mjt.splitwise.models.User;
import bg.sofia.uni.fmi.mjt.splitwise.server.Session;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.UserRepository;

import java.util.List;

public class RegisterCommand extends AbstractCommand {
    private static final String SUCCESS_RESPONSE = "Successfully registered!";

    private final UserRepository userRepository;

    public RegisterCommand(UserRepository userRepository) {
        super(userRepository, 2,2);
        this.userRepository = userRepository;
    }

    @Override
    public String run(List<String> arguments, Session session) throws UserExistsException {
        String username = arguments.get(0);
        String password = arguments.get(1);

        List<User> users = userRepository.getUsers();

        boolean userExists = userRepository.doesUserExist(username);
        if (userExists) {
            throw new UserExistsException();
        }

        users.add(new User(username, password));

        userRepository.replaceAll(users);
        return SUCCESS_RESPONSE;
    }

}
