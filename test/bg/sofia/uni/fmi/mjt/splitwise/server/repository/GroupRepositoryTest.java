package bg.sofia.uni.fmi.mjt.splitwise.server.repository;

import bg.sofia.uni.fmi.mjt.splitwise.models.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupRepositoryTest {

    @Mock
    GroupFileRepository fileRepository;

    @Test
    void testConstructorShouldThrowWhenRepositoryIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new GroupRepository(null),
                "Expect tot throw");
    }

    @Test
    void testGetGroupsWithUsersShouldReturnGroupsBySize() {
        var users1 = Set.of("u1", "u2", "u3");
        var users2 = Set.of("u1", "u2");
        var users3 = Set.of("u4", "u5");
        var g1 = new Group("g1", users1);
        var g2 = new Group("g2", users2);
        var g3 = new Group("g3", users3);

        when(fileRepository.getGroups()).thenReturn(List.of(g1, g2, g3));

        var repository = new GroupRepository(fileRepository);
        assertIterableEquals(List.of(g2, g1), repository.getGroupsWithUsers(List.of("u1", "u2")),
                "Expect to get groups sorted by size");
    }

    @Test
    void testGetFriendshipGroupShouldReturnExactMatch() {
        var users1 = Set.of("u1", "u2");
        var users2 = Set.of("u1", "u2", "u3");
        var g1 = new Group("g1", users1);
        var g2 = new Group("g2", users2);

        when(fileRepository.getGroups()).thenReturn(List.of(g1, g2));

        var repository = new GroupRepository(fileRepository);
        assertEquals(g1, repository.getFriendshipGroup("u1", "u2"),
                "Expect to get g1");
    }

    @Test
    void testGetGroupOfUserByNameShouldAllowsSameNameWithDifferentUsers() {
        String sameName = "group";
        var users1 = Set.of("u1", "u2", "u3");
        var users2 = Set.of("u4", "u5", "u6");
        var g1 = new Group(sameName, users1);
        var g2 = new Group(sameName, users2);

        when(fileRepository.getGroups()).thenReturn(List.of(g1, g2));

        var repository = new GroupRepository(fileRepository);
        assertEquals(g1, repository.getGroupOfUserByName(sameName, "u1"),
                "Expect to get g1");

        assertEquals(g2, repository.getGroupOfUserByName(sameName, "u4"),
                "Expect to get g2");
    }

    @Test
    void testDoesGroupWithUsersExistShouldReturnTrueWhenExists() {
        var users = Set.of("u1", "u2", "u3");
        var group = new Group("g", users);

        when(fileRepository.getGroups()).thenReturn(List.of(group));

        var repository = new GroupRepository(fileRepository);
        assertTrue(repository.doesGroupWithUsersExist(users),
                "Expect result to be true");
    }

    @Test
    void testUpdateGroupShouldCallInternalUpdate() {
        var group = new Group("g", "u1");

        var repository = new GroupRepository(fileRepository);
        repository.updateGroup(group);

        verify(fileRepository, times(1)).updateGroup(group);
    }

}
