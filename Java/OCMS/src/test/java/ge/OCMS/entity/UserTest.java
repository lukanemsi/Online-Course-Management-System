package ge.OCMS.entity;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    @Test
    void getId_whenSetId_thenReturnsId() {
        User user = new User();
        Long expectedId = 1L;

        user.setId(expectedId);
        Long actualId = user.getId();

        assertEquals(expectedId, actualId);
    }

    @Test
    void getUsername_whenSetUsername_thenReturnsUsername() {
        User user = new User();
        String expectedUsername = "username";

        user.setUsername(expectedUsername);
        String actualUsername = user.getUsername();

        assertEquals(expectedUsername, actualUsername);
    }

    @Test
    void getPassword_whenSetPassword_thenReturnsPassword() {
        User user = new User();
        String expectedPassword = "password123";

        user.setPassword(expectedPassword);
        String actualPassword = user.getPassword();

        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    void getAuthorities_whenSetRoles_thenReturnsAuthorities() {
        User user = new User();
        Role role = new Role();
        role.setRoleName("STUDENT");

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        Set<GrantedAuthority> expectedAuthorities = new HashSet<>();
        expectedAuthorities.add(new SimpleGrantedAuthority("STUDENT"));

        Collection<? extends GrantedAuthority> actualAuthorities = user.getAuthorities();

        assertTrue(actualAuthorities.containsAll(expectedAuthorities));
        assertEquals(expectedAuthorities.size(), actualAuthorities.size());
    }

    @Test
    void getAuthorities_whenNoRoles_thenReturnsEmptyAuthorities() {
        User user = new User();
        user.setRoles(new HashSet<>());

        Collection<? extends GrantedAuthority> actualAuthorities = user.getAuthorities();

        assertTrue(actualAuthorities.isEmpty());
    }

    @Test
    void equals_whenSameId_thenReturnsTrue() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(1L);

        assertEquals(user1, user2);
    }

    @Test
    void equals_whenDifferentId_thenReturnsFalse() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        assertNotEquals(user1, user2);
    }

    @Test
    void hashCode_whenSameId_thenReturnsSameHashCode() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(1L);

        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void hashCode_whenDifferentId_thenReturnsDifferentHashCode() {
        User user1 = new User();
        user1.setId(1L);

        User user2 = new User();
        user2.setId(2L);

        assertNotEquals(user1.hashCode(), user2.hashCode());
    }
}
