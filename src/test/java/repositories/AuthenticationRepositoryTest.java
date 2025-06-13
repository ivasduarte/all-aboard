package repositories;

import domain.User;
import domain.UserSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

class AuthenticationRepositoryTest {
    private AuthenticationRepository repository;
    private User user1;
    private User user2;
    private UserSession session;

    @BeforeEach
    void setUp() {
        repository = new AuthenticationRepository();
        user1 = new User("User 1", "user1@example.com", "PLAYER", "password1");
        user2 = new User("User 2", "user2@example.com", "PLAYER", "password2");
        session = UserSession.getInstance();
        session.doLogout();
    }

    @Test
    void testCreateNewUser() {
        boolean result = repository.createNewUser(user1);
        assertTrue(result);
        List<User> users = repository.getAllUsers();
        assertEquals(1, users.size());
        assertTrue(users.contains(user1));
    }

    @Test
    void testCreateDuplicateUser() {
        repository.createNewUser(user1);
        boolean result = repository.createNewUser(user1);
        assertFalse(result);
        List<User> users = repository.getAllUsers();
        assertEquals(1, users.size());
    }

    @Test
    void testGetAllUsers() {
        repository.createNewUser(user1);
        repository.createNewUser(user2);
        List<User> users = repository.getAllUsers();
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    void testGetUserByEmail() {
        repository.createNewUser(user1);
        Optional<User> found = repository.getUserByEmail("user1@example.com");
        assertTrue(found.isPresent());
        assertEquals(user1, found.get());
    }

    @Test
    void testGetUserByEmailNotFound() {
        Optional<User> found = repository.getUserByEmail("nonexistent@example.com");
        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteUserByEmailNotFound() {
        boolean result = repository.deleteUser("nonexistent@example.com");
        assertFalse(result);
    }

    @Test
    void testDeleteUserByObjectNotFound() {
        boolean result = repository.deleteUser(user1);
        assertFalse(result);
    }

    @Test
    void testChangeRoleByEmail() {
        repository.createNewUser(user1);
        boolean result = repository.changeRole("user1@example.com", "ADMIN");
        assertTrue(result);
        Optional<User> updatedUser = repository.getUserByEmail("user1@example.com");
        assertTrue(updatedUser.isPresent());
        assertEquals("ADMIN", updatedUser.get().getRoleId());
    }

    @Test
    void testChangeRoleByEmailNotFound() {
        boolean result = repository.changeRole("nonexistent@example.com", "ADMIN");
        assertFalse(result);
    }

    @Test
    void testChangeRoleByObject() {
        repository.createNewUser(user1);
        boolean result = repository.changeRole(user1, "ADMIN");
        assertTrue(result);
        Optional<User> updatedUser = repository.getUserByEmail("user1@example.com");
        assertTrue(updatedUser.isPresent());
        assertEquals("ADMIN", updatedUser.get().getRoleId());
    }

    @Test
    void testChangeRoleByObjectNotFound() {
        boolean result = repository.changeRole(user1, "ADMIN");
        assertFalse(result);
    }

    @Test
    void testDoLogin() {
        repository.createNewUser(user1);
        boolean result = repository.doLogin("user1@example.com", "password1");
        assertTrue(result);
        assertEquals(user1, session.getUser());
    }

    @Test
    void testDoLoginInvalidCredentials() {
        repository.createNewUser(user1);
        boolean result = repository.doLogin("user1@example.com", "wrongpassword");
        assertFalse(result);
        assertNull(session.getUser());
    }

    @Test
    void testDoLogout() {
        repository.createNewUser(user1);
        repository.doLogin("user1@example.com", "password1");
        boolean result = repository.doLogout();
        assertTrue(result);
        assertNull(session.getUser());
    }

    @Test
    void testGetRole() {
        repository.createNewUser(user1);
        String role = repository.getRole("user1@example.com");
        assertEquals("PLAYER", role);
    }

    @Test
    void testGetRoleNotFound() {
        String role = repository.getRole("nonexistent@example.com");
        assertNull(role);
    }

    @Test
    void testGetCurrentUserEmail() {
        repository.createNewUser(user1);
        repository.doLogin("user1@example.com", "password1");
        String email = repository.getCurrentUserEmail();
        assertEquals("user1@example.com", email);
    }
} 