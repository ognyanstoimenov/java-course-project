package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.SessionException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.WrongNumberOfArgumentsException;
import bg.sofia.uni.fmi.mjt.splitwise.server.Session;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.UserRepository;

import java.util.List;

public abstract class AbstractCommand implements Command {
    private final int minArguments;
    private final int maxArguments;

    protected final UserRepository userRepository;

    protected AbstractCommand(UserRepository userRepository, int minArguments, int maxArguments) {
        this.minArguments = minArguments;
        this.maxArguments = maxArguments;
        this.userRepository = userRepository;
    }

    private void validateArguments(List<String> arguments) throws WrongNumberOfArgumentsException {
        if (arguments == null) {
            throw new IllegalArgumentException("Arguments shouldn't be null");
        }

        if (arguments.size() < minArguments || arguments.size() > maxArguments) {
            throw new WrongNumberOfArgumentsException(arguments.size());
        }

        for (String argument : arguments) {
            if (argument.isEmpty()) {
                throw new IllegalArgumentException("Argument shouldn't be empty");
            }
        }
    }

    protected void validateArguments(List<String> arguments, Session session, boolean shouldBeLoggedIn)
            throws WrongNumberOfArgumentsException, SessionException {
        validateArguments(arguments);

        if (session == null) {
            throw new IllegalArgumentException("Session shouldn't be null.");
        }

        if ((session.getCurrentUser() == null) == shouldBeLoggedIn) {
            throw new SessionException(shouldBeLoggedIn);
        }
    }
}