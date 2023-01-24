package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.GroupExistsException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserDoesnotExistException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.WrongNumberOfArgumentsException;
import bg.sofia.uni.fmi.mjt.splitwise.server.Session;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.GroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateGroupCommandTest {

    @Mock
    UserRepository userRepository;

    @Mock
    GroupRepository groupRepository;

    @Mock
    Session session;

    @Test
    void testRunShouldThrowWhenArgumentsIsNull() {
        var command = new CreateGroupCommand(userRepository, groupRepository);
        assertThrows(IllegalArgumentException.class, () -> command.run(null, session),
                "Expect to throw.");
    }

    @Test
    void testRunShouldThrowWhenSessionIsNull() {
        var command = new CreateGroupCommand(userRepository, groupRepository);
        assertThrows(IllegalArgumentException.class, () -> command.run(List.of("g", "u1", "u2"), null),
                "Expect to throw.");
    }

    @Test
    void testRunShouldThrowWhenInvalidNumberOfArguments() {
        var command = new CreateGroupCommand(userRepository, groupRepository);
        assertThrows(WrongNumberOfArgumentsException.class, () -> command.run(List.of("1"), session),
                "Expect to throw.");

    }

    @Test
    void testRunShouldThrowWhenGroupExists() {
        String currentUser = "user1";
        String group = "group";
        String user2 = "user2";
        String user3 = "user3";
        String user4 = "user4";
        List<String> args = List.of(group, user2, user3, user4);

        when(session.getCurrentUser()).thenReturn(currentUser);
        when(groupRepository.doesGroupWithUsersExist(any())).thenReturn(true);

        var command = new CreateGroupCommand(userRepository, groupRepository);
        assertThrows(GroupExistsException.class, () -> command.run(args, session),
                "Expect to throw.");

    }

    @Test
    void testRunShouldThrowWhenAUserDoesNotExist() {
        String currentUser = "user1";
        String group = "group";
        String user2 = "user2";
        String user3 = "user3";
        String user4 = "user4";
        List<String> args = List.of(group, user2, user3, user4);

        when(session.getCurrentUser()).thenReturn(currentUser);
        when(userRepository.doesUserExist(any())).thenReturn(false);

        var command = new CreateGroupCommand(userRepository, groupRepository);
        assertThrows(UserDoesnotExistException.class, () -> command.run(args, session),
                "Expect to throw.");

    }

    @Test
    void testRunShouldThrowWhenUsersAreLessThanThree() {
        String currentUser = "user1";
        String group = "group";
        String user2 = "user2";
        List<String> args = List.of(group, user2);

        when(session.getCurrentUser()).thenReturn(currentUser);
        when(userRepository.doesUserExist(any())).thenReturn(true);

        var command = new CreateGroupCommand(userRepository, groupRepository);
        assertThrows(IllegalArgumentException.class, () -> command.run(args, session),
                "Expect to throw.");

    }

    @Test
    void testRunShouldCallUpdateGroupWhenSuccessful() throws CommandException {
        String currentUser = "user1";
        String group = "group";
        String user2 = "user2";
        String user3 = "user3";
        String user4 = "user4";
        List<String> args = List.of(group, user2, user3, user4);

        when(session.getCurrentUser()).thenReturn(currentUser);
        when(userRepository.doesUserExist(any())).thenReturn(true);

        var command = new CreateGroupCommand(userRepository, groupRepository);
        command.run(args, session);

        verify(groupRepository, times(1)).updateGroup(any());
    }

}
