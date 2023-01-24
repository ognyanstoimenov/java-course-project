package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetStatusCommandTest {

    @Mock
    UserRepository userRepository;

    @Mock
    GroupRepository groupRepository;

    @Mock
    Session session;

    @Mock
    Group groupMock;

    @Test
    void testRunShouldThrowWhenArgumentsIsNull() {
        var command = new GetStatusCommand(userRepository, groupRepository);
        assertThrows(IllegalArgumentException.class, () -> command.run(null, session),
                "Expect to throw.");
    }

    @Test
    void testRunShouldThrowWhenSessionIsNull() {
        var command = new GetStatusCommand(userRepository, groupRepository);
        assertThrows(IllegalArgumentException.class, () -> command.run(List.of(), null),
                "Expect to throw.");
    }

    @Test
    void testRunShouldThrowWhenInvalidNumberOfArguments() {
        var command = new GetStatusCommand(userRepository, groupRepository);
        assertThrows(WrongNumberOfArgumentsException.class, () -> command.run(List.of("1"), session),
                "Expect to throw.");

    }

    @Test
    void testRunShouldCallGroupCalculations() throws CommandException {
        String currentUser = "user1";

        when(session.getCurrentUser()).thenReturn(currentUser);
        when(groupRepository.getGroupsWithUsers(any())).thenReturn(List.of(groupMock));

        var command = new GetStatusCommand(userRepository, groupRepository);
        command.run(List.of(), session);

        verify(groupRepository, times(1)).getGroupsWithUsers(any());
        verify(groupMock, atLeast(1)).getTransactions();
    }

}
