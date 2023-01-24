package bg.sofia.uni.fmi.mjt.splitwise.server.repository;

import bg.sofia.uni.fmi.mjt.splitwise.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    UserFileRepository fileRepository;

    @Test
    void testConstructorShouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new UserRepository(null),
                "Expect tot throw");
    }

    @Test
    void getUsersShouldCallInternalGet() {
        var repository = new UserRepository(fileRepository);
        repository.getUsers();

        verify(fileRepository, times(1)).getUsers();
    }

    @Test
    void doesUserExistWithPasswordShouldReturnTrueWhenUserExists() {
        var users = List.of(
                new User("u1", "p"),
                new User("u1", "p")
        );

        when(fileRepository.getUsers()).thenReturn(users);

        var repository = new UserRepository(fileRepository);
        assertTrue(repository.doesUserExist("u1", "p"), "Expect to be true");
    }

    @Test
    void doesUserExistShouldReturnTrueWhenUserExists() {
        var users = List.of(
                new User("u1", "p"),
                new User("u1", "p")
        );

        when(fileRepository.getUsers()).thenReturn(users);

        var repository = new UserRepository(fileRepository);
        assertTrue(repository.doesUserExist("u1"), "Expect to be true");
    }

    @Test
    void doesUserExistShouldReturnFalseWhenUserDoesNotExist() {
        var users = List.of(
                new User("u1", "p"),
                new User("u1", "p")
        );

        when(fileRepository.getUsers()).thenReturn(users);

        var repository = new UserRepository(fileRepository);
        assertFalse(repository.doesUserExist("u4"), "Expect to be false");
    }

    @Test
    void replaceAllShouldCallInternalReplace() {
        var users = List.of(
                new User("u1", "p"),
                new User("u1", "p")
        );

        var repository = new UserRepository(fileRepository);
        repository.replaceAll(users);

        verify(fileRepository, times(1)).replaceAll(users);
    }

}