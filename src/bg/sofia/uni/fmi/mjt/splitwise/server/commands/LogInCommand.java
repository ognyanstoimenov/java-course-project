package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.IncorrectCredentialsException;
import bg.sofia.uni.fmi.mjt.splitwise.server.Session;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.UserRepository;

import java.util.List;

public class LogInCommand extends AbstractCommand {
    private static final String SUCCESS_RESPONSE = "Logged in as %s.";
    public LogInCommand(UserRepository userRepository) {
        super(userRepository, 2, 2);
    }

    @Override
    public String run(List<String> arguments, Session session) throws CommandException {
        validateArguments(arguments, session, false);

        String username = arguments.get(0);
        String password = arguments.get(1);

        if (!userRepository.doesUserExist(username, password)) {
            throw new IncorrectCredentialsException();
        }


        userRepository.logInUser(username);
        session.setCurrentUser(username);

        return SUCCESS_RESPONSE.formatted(username);
    }
}
