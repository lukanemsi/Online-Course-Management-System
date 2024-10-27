package ge.OCMS.util;

import ge.OCMS.wrapper.dto.UserDTO;
import ge.OCMS.wrapper.dto.RoleDTO;

import java.util.HashSet;
import java.util.Set;

public class UserMaskingUtil {

    private static final String MASKED_PASSWORD = "****";
    private static final String MASKED_ROLE = "RESTRICTED";

    private UserMaskingUtil() {
    }

    public static UserDTO maskUser(UserDTO user) {
        UserDTO maskedUser = new UserDTO();
        maskedUser.setUsername(user.getUsername());
        maskedUser.setPassword(MASKED_PASSWORD);

        Set<RoleDTO> maskedRoles = new HashSet<>();
        if(!user.getRoles().isEmpty()) {
            RoleDTO maskedRole = new RoleDTO();
            maskedRole.setName(MASKED_ROLE);
            maskedRoles.add(maskedRole);
        }
        maskedUser.setRoles(maskedRoles);

        return maskedUser;
    }
}
