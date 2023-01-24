package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.server.Session;

import java.util.List;

public interface Command {
    String run(List<String> arguments, Session session) throws CommandException;
}
