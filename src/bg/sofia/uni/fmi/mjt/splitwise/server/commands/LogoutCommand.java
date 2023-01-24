package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.server.Session;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.UserRepository;

import java.util.List;

public class LogoutCommand extends AbstractCommand {
    private static final String SUCCESS_RESPONSE = "Successfully logged out";
    public LogoutCommand(UserRepository userRepository) {
        super(userRepository, 0, 0);
    }

    @Override
    public String run(List<String> arguments, Session session) throws CommandException {
        validateArguments(arguments, session, true);

        session.setCurrentUser(null);

        return SUCCESS_RESPONSE;
    }
}
