package ge.OCMS.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void getId_whenSetId_thenReturnsId() {
        Role role = new Role();
        Long expectedId = 1L;

        role.setId(expectedId);
        Long actualId = role.getId();

        assertEquals(expectedId, actualId);
    }

    @Test
    void getRoleName_whenSetRoleName_thenReturnsRoleName() {
        Role role = new Role();
        String expectedRoleName = "STUDENT";

        role.setRoleName(expectedRoleName);
        String actualRoleName = role.getRoleName();

        assertEquals(expectedRoleName, actualRoleName);
    }

    @Test
    void equals_whenSameId_thenReturnsTrue() {
        Role role1 = new Role();
        role1.setId(1L);

        Role role2 = new Role();
        role2.setId(1L);

        boolean areEqual = role1.equals(role2);

        assertTrue(areEqual);
    }

    @Test
    void equals_whenDifferentId_thenReturnsFalse() {
        Role role1 = new Role();
        role1.setId(1L);

        Role role2 = new Role();
        role2.setId(2L);

        boolean areEqual = role1.equals(role2);

        assertFalse(areEqual);
    }

    @Test
    void hashCode_whenSameId_thenReturnsSameHashCode() {
        Role role1 = new Role();
        role1.setId(1L);

        Role role2 = new Role();
        role2.setId(1L);

        int hashCode1 = role1.hashCode();
        int hashCode2 = role2.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void hashCode_whenDifferentId_thenReturnsDifferentHashCode() {
        Role role1 = new Role();
        role1.setId(1L);

        Role role2 = new Role();
        role2.setId(2L);

        int hashCode1 = role1.hashCode();
        int hashCode2 = role2.hashCode();

        assertNotEquals(hashCode1, hashCode2);
    }
}
