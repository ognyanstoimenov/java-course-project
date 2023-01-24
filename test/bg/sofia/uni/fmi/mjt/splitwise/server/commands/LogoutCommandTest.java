package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.SessionException;
import bg.sofia.uni.fmi.mjt.splitwise.server.Session;
import bg.sofia.uni.fmi.mjt.splitwise.server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutCommandTest {

    @Mock
    UserRepository userRepository;

    @Mock
    Session session;

    @Test
    void testRunShouldThrowWhenNotLoggedIn() {
        when(session.getCurrentUser()).thenReturn(null);

        var command = new LogoutCommand(userRepository);
        assertThrows(SessionException.class, () -> command.run(List.of(), session));
    }

    @Test
    void testRunShouldCallSessionLogout() throws CommandException {
        String username = "user";

        when(session.getCurrentUser()).thenReturn(username);

        var command = new LogoutCommand(userRepository);
        command.run(List.of(), session);

        verify(session, times(1)).setCurrentUser(null);
    }

}
