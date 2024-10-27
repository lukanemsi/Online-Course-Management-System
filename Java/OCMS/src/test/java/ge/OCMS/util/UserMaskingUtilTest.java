package ge.OCMS.util;

import ge.OCMS.wrapper.dto.RoleDTO;
import ge.OCMS.wrapper.dto.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserMaskingUtilTest {

    @Test
    void maskUser_whenCalled_thenMasksPassword() {
        UserDTO user = new UserDTO();
        user.setUsername("testUser");
        user.setPassword("originalPassword");

        UserDTO maskedUser = UserMaskingUtil.maskUser(user);

        assertEquals("****", maskedUser.getPassword());
    }

    @Test
    void maskUser_whenCalled_thenMasksRoles() {
        UserDTO user = new UserDTO();
        user.setUsername("testUser");
        user.setPassword("originalPassword");

        Set<RoleDTO> roles = new HashSet<>();
        RoleDTO role = new RoleDTO();
        role.setName("STUDENT");
        roles.add(role);

        user.setRoles(roles);

        UserDTO maskedUser = UserMaskingUtil.maskUser(user);

        assertNotNull(maskedUser.getRoles());
        assertEquals(1, maskedUser.getRoles().size());
        assertEquals("RESTRICTED", maskedUser.getRoles().iterator().next().getName());
    }

    @Test
    void maskUser_whenCalled_thenKeepsOriginalUsername() {
        UserDTO user = new UserDTO();
        user.setUsername("testUser");
        user.setPassword("originalPassword");

        UserDTO maskedUser = UserMaskingUtil.maskUser(user);

        assertEquals("testUser", maskedUser.getUsername());
    }

    @Test
    void maskUser_whenUserHasMultipleRoles_thenMasksAllRoles() {
        UserDTO user = new UserDTO();
        user.setUsername("testUser");
        user.setPassword("originalPassword");

        Set<RoleDTO> roles = new HashSet<>();
        RoleDTO role1 = new RoleDTO();
        role1.setName("STUDENT");
        RoleDTO role2 = new RoleDTO();
        role2.setName("INSTRUCTOR");
        roles.add(role1);
        roles.add(role2);

        user.setRoles(roles);

        UserDTO maskedUser = UserMaskingUtil.maskUser(user);

        assertEquals(1, maskedUser.getRoles().size());
        for (RoleDTO role : maskedUser.getRoles()) {
            assertEquals("RESTRICTED", role.getName());
        }
    }
}
