package bg.sofia.uni.fmi.mjt.splitwise.server;

import bg.sofia.uni.fmi.mjt.splitwise.config.Config;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.RegisterCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.Command;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.CreateGroupCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.LogInCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.LogoutCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.AddFriendCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.SplitCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.SplitGroupCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.PayedCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.commands.GetStatusCommand;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.GroupFileRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.GroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.UserFileRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.UserRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServerMain {

    public static void main(String[] args) throws IOException {
        UserRepository userRepository = new UserRepository(new UserFileRepository(Config.USERS_PATH));
        GroupRepository groupRepository = new GroupRepository(new GroupFileRepository(Config.GROUPS_FOLDER));

        Map<String, Command> stringCommandMap = new HashMap<>();
        stringCommandMap.put("register", new RegisterCommand(userRepository));
        stringCommandMap.put("login", new LogInCommand(userRepository));
        stringCommandMap.put("logout", new LogoutCommand(userRepository));
        stringCommandMap.put("add-friend", new AddFriendCommand(userRepository, groupRepository));
        stringCommandMap.put("create-group", new CreateGroupCommand(userRepository, groupRepository));
        stringCommandMap.put("split", new SplitCommand(userRepository, groupRepository));
        stringCommandMap.put("payed", new PayedCommand(userRepository, groupRepository));
        stringCommandMap.put("get-status", new GetStatusCommand(userRepository, groupRepository));
        stringCommandMap.put("split-group", new SplitGroupCommand(userRepository, groupRepository));

        CommandManager commandManager = new CommandManager(stringCommandMap);
        SplitwiseServer server = new SplitwiseServer(commandManager);
        server.run();
    }

}
