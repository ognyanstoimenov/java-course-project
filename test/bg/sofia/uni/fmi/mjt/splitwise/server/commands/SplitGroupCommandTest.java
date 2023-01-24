package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.GroupDoesNotExistException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.WrongNumberOfArgumentsException;
import bg.sofia.uni.fmi.mjt.splitwise.models.Group;
import bg.sofia.uni.fmi.mjt.splitwise.server.Session;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.GroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SplitGroupCommandTest {

    @Mock
    UserRepository userRepository;

    @Mock
    GroupRepository groupRepository;

    @Mock
    Session session;

    @Test
    void testRunShouldThrowWhenArgumentsIsNull() {
        var command = new SplitGroupCommand(userRepository, groupRepository);
        assertThrows(IllegalArgumentException.class, () -> command.run(null, session),
                "Expect to throw.");
    }

    @Test
    void testRunShouldThrowWhenSessionIsNull() {
        var command = new SplitGroupCommand(userRepository, groupRepository);
        assertThrows(IllegalArgumentException.class, () -> command.run(List.of("1", "2", "3"), null),
                "Expect to throw.");
    }

    @Test
    void testRunShouldThrowWhenInvalidNumberOfArguments() {
        var command = new SplitGroupCommand(userRepository, groupRepository);
        assertThrows(WrongNumberOfArgumentsException.class, () -> command.run(List.of("1", "2"), session),
                "Expect to throw.");

    }

    @Test
    void testRunShouldThrowWhenUserDoesNotExist() {
        String currentUser = "user1";
        String amount = "5.0";
        String groupName = "group";
        String reason = "rr";

        when(groupRepository.getGroupOfUserByName(groupName, currentUser)).thenReturn(null);
        when(session.getCurrentUser()).thenReturn(currentUser);

        var command = new SplitGroupCommand(userRepository, groupRepository);
        assertThrows(GroupDoesNotExistException.class, () -> command.run(List.of(amount, groupName, reason), session),
                "Expect to throw.");

    }

    @Test
    void testRunShouldCallUpdateGroupWhenSuccessful() throws CommandException {
        String currentUser = "user1";
        String amount = "5.0";
        String groupName = "group";
        String reason = "rr";
        var group = new Group(groupName, Set.of());

        when(groupRepository.getGroupOfUserByName(groupName, currentUser)).thenReturn(group);
        when(session.getCurrentUser()).thenReturn(currentUser);

        var command = new SplitGroupCommand(userRepository, groupRepository);
        command.run(List.of(amount, groupName, reason), session);

        verify(groupRepository, times(1)).updateGroup(any());
    }

}
