package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.FriendshipDoesNotExist;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserDoesnotExistException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.WrongNumberOfArgumentsException;
import bg.sofia.uni.fmi.mjt.splitwise.models.Group;
import bg.sofia.uni.fmi.mjt.splitwise.models.Transaction;
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
class PayedCommandTest {

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
        var command = new PayedCommand(userRepository, groupRepository);
        assertThrows(IllegalArgumentException.class, () -> command.run(null, session),
                "Expect to throw.");
    }

    @Test
    void testRunShouldThrowWhenSessionIsNull() {
        var command = new PayedCommand(userRepository, groupRepository);
        assertThrows(IllegalArgumentException.class, () -> command.run(List.of("1", "2", "3"), null),
                "Expect to throw.");
    }

    @Test
    void testRunShouldThrowWhenInvalidNumberOfArguments() {
        var command = new PayedCommand(userRepository, groupRepository);
        assertThrows(WrongNumberOfArgumentsException.class, () -> command.run(List.of("1", "2", "3", "4"), session),
                "Expect to throw.");

    }

    @Test
    void testRunShouldThrowWhenUserDoesNotExist() {
        String currentUser = "user1";
        String amount = "5.0";
        String otherUser = "user2";
        String groupName = "g1";

        when(session.getCurrentUser()).thenReturn(currentUser);

        var command = new PayedCommand(userRepository, groupRepository);
        assertThrows(UserDoesnotExistException.class, () -> command.run(List.of(amount, otherUser, groupName), session),
                "Expect to throw.");

    }

    @Test
    void testRunShouldCallGetGroupOfUserWhenThreeArguments() throws CommandException {
        String currentUser = "user1";
        String amount = "2.0";
        String otherUser = "user2";
        String groupName = "g1";

        when(groupMock.getUsers()).thenReturn(Set.of(currentUser, otherUser));
        when(groupMock.getTransactions()).thenReturn(List.of(new Transaction(currentUser, 5, "c")));

        when(session.getCurrentUser()).thenReturn(currentUser);
        when(userRepository.doesUserExist(otherUser)).thenReturn(true);
        when(groupRepository.getGroupOfUserByName(groupName, currentUser))
                .thenReturn(groupMock);

        var command = new PayedCommand(userRepository, groupRepository);
        command.run(List.of(amount, otherUser, groupName), session);

        verify(groupRepository, times(1)).getGroupOfUserByName(groupName, currentUser);
    }

}
