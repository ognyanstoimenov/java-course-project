package bg.sofia.uni.fmi.mjt.splitwise.server.commands;

import bg.sofia.uni.fmi.mjt.splitwise.exceptions.CommandException;
import bg.sofia.uni.fmi.mjt.splitwise.exceptions.UserExistsException;
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
class RegisterCommandTest {

    @Mock
    UserRepository userRepository;

    @Mock
    Session session;

    @Test
    void testRunShouldThrowWhenUserExists() {
        String username = "user";
        String password = "pass";

        when(userRepository.doesUserExist(username)).thenReturn(true);

        var command = new RegisterCommand(userRepository);
        assertThrows(UserExistsException.class, () -> command.run(List.of(username, password), session),
                "Expect to throw.");

    }

    @Test
    void testRunShouldCallRepositoryWhenSuccessful() throws CommandException {
        String username = "user";
        String password = "pass";

        var command = new RegisterCommand(userRepository);
        command.run(List.of(username, password), session);

        verify(userRepository, times(1)).replaceAll(any());
    }

}
