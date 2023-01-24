package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.AlreadyFriendsException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserDoesnotExistException;
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
class AddFriendCommandTest {

    @Mock
    UserRepository userRepository;

    @Mock
    GroupRepository groupRepository;

    @Mock
    Session session;

    @Test
    void testRunShouldThrowWhenArgumentsIsNull() {
        var command = new AddFriendCommand(userRepository, groupRepository);
        assertThrows(IllegalArgumentException.class, () -> command.run(null, session),
                "Expect to throw.");
    }

    @Test
    void testRunShouldThrowWhenSessionIsNull() {
        var command = new AddFriendCommand(userRepository, groupRepository);
        assertThrows(IllegalArgumentException.class, () -> command.run(List.of("u1"), null),
                "Expect to throw.");
    }

    @Test
    void testRunShouldThrowWhenAlreadyFriends() {
       String currentUser = "user1";
       String friend = "user2";

       when(session.getCurrentUser()).thenReturn(currentUser);
       when(groupRepository.doesGroupWithUsersExist(any())).thenReturn(true);

        var command = new AddFriendCommand(userRepository, groupRepository);
        assertThrows(AlreadyFriendsException.class, () -> command.run(List.of(friend), session),
                "Expect to throw.");

    }

    @Test
    void testRunShouldThrowWhenFriendDoesNotExist() {
        String currentUser = "user1";
        String friend = "user2";

        when(session.getCurrentUser()).thenReturn(currentUser);
        when(userRepository.doesUserExist(friend)).thenReturn(false);

        var command = new AddFriendCommand(userRepository, groupRepository);
        assertThrows(UserDoesnotExistException.class, () -> command.run(List.of(friend), session),
                "Expect to throw.");

    }

    @Test
    void testRunShouldCallRepositoryWhenSuccessful() throws CommandException {
        String currentUser = "user1";
        String friend = "user2";

        when(session.getCurrentUser()).thenReturn(currentUser);
        when(userRepository.doesUserExist(friend)).thenReturn(true);

        var command = new AddFriendCommand(userRepository, groupRepository);
        command.run(List.of(friend), session);

        verify(groupRepository, times(1)).updateGroup(any());
    }

}
