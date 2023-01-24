package bg.sofia.uni.fmi.mjt.splitwise.server;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UnknownCommandException;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Command;

import java.util.Map;

public class CommandManager {
    private final Map<String, Command> commands;


    public CommandManager(Map<String, Command> commands) {
        if (commands == null) {
            throw new IllegalArgumentException("Commands can't be null.");
        }

        this.commands = commands;


    }

    public Command getCommand(String commandString) throws UnknownCommandException {
        if (commandString == null || commandString.isEmpty()) {
            throw new IllegalArgumentException("Command shouldn't be empty.");
        }

        Command command = commands.get(commandString);

        if (command == null) {
            throw new UnknownCommandException();
        }

        return command;
    }
}
