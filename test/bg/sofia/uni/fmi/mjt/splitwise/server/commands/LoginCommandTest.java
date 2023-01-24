package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.IncorrectCredentialsException;
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
class LoginCommandTest {

    @Mock
    UserRepository userRepository;

    @Mock
    Session session;

    @Test
    void testRunShouldThrowWhenAlreadyLoggedIn() {
        String username = "user";
        String password = "pass";

        when(session.getCurrentUser()).thenReturn(username);

        var command = new LogInCommand(userRepository);
        assertThrows(SessionException.class, () -> command.run(List.of(username, password), session));
    }

    @Test
    void testRunShouldThrowWhenIncorrectCredentials() {
        String username = "user";
        String password = "pass";

        when(session.getCurrentUser()).thenReturn(null);
        when(userRepository.doesUserExist(username, password)).thenReturn(false);

        var command = new LogInCommand(userRepository);
        assertThrows(IncorrectCredentialsException.class, () -> command.run(List.of(username, password), session));
    }

    @Test
    void testRunShouldCallRepositoryAndSessionLogin() throws CommandException {
        String username = "user";
        String password = "pass";

        when(session.getCurrentUser()).thenReturn(null);
        when(userRepository.doesUserExist(username, password)).thenReturn(true);

        var command = new LogInCommand(userRepository);
        command.run(List.of(username, password), session);

        verify(userRepository, times(1)).logInUser(username);
        verify(session, times(1)).setCurrentUser(username);
    }

}
