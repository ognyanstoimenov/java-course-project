package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.FriendshipDoesNotExist;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserDoesnotExistException;
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
class SplitCommandTest {

    @Mock
    UserRepository userRepository;

    @Mock
    GroupRepository groupRepository;

    @Mock
    Session session;

    @Test
    void testRunShouldThrowWhenArgumentsIsNull() {
        var command = new SplitCommand(userRepository, groupRepository);
        assertThrows(IllegalArgumentException.class, () -> command.run(null, session),
                "Expect to throw.");
    }

    @Test
    void testRunShouldThrowWhenSessionIsNull() {
        var command = new SplitCommand(userRepository, groupRepository);
        assertThrows(IllegalArgumentException.class, () -> command.run(List.of("1", "2", "3"), null),
                "Expect to throw.");
    }

    @Test
    void testRunShouldThrowWhenInvalidNumberOfArguments() {
        var command = new SplitCommand(userRepository, groupRepository);
        assertThrows(WrongNumberOfArgumentsException.class, () -> command.run(List.of("1", "2"), session),
                "Expect to throw.");

    }

    @Test
    void testRunShouldThrowWhenUserDoesNotExist() {
        String currentUser = "user1";
        String amount = "5.0";
        String otherUser = "user2";
        String reason = "rr";

        when(session.getCurrentUser()).thenReturn(currentUser);

        var command = new SplitCommand(userRepository, groupRepository);
        assertThrows(UserDoesnotExistException.class, () -> command.run(List.of(amount, otherUser, reason), session),
                "Expect to throw.");

    }

    @Test
    void testRunShouldThrowWhenFriendshipDoesNotExist() {
        String currentUser = "user1";
        String amount = "5.0";
        String otherUser = "user2";
        String reason = "rr";

        when(session.getCurrentUser()).thenReturn(currentUser);
        when(userRepository.doesUserExist(otherUser)).thenReturn(true);

        var command = new SplitCommand(userRepository, groupRepository);
        assertThrows(FriendshipDoesNotExist.class, () -> command.run(List.of(amount, otherUser, reason), session),
                "Expect to throw.");

    }

    @Test
    void testRunShouldCallUpdateGroupWhenSuccessful() throws CommandException {
        String currentUser = "user1";
        String amount = "5.0";
        String otherUser = "user2";
        String reason = "rr";

        when(session.getCurrentUser()).thenReturn(currentUser);
        when(userRepository.doesUserExist(otherUser)).thenReturn(true);
        when(groupRepository.getFriendshipGroup(currentUser, otherUser))
                .thenReturn(new Group(currentUser, otherUser));

        var command = new SplitCommand(userRepository, groupRepository);
        command.run(List.of(amount, otherUser, reason), session);

        verify(groupRepository, times(1)).updateGroup(any());
    }

}
