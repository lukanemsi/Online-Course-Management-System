package ge.OCMS.wrapper.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDTO {
    private String username;
    private String password;
    private Set<RoleDTO> roles = new HashSet<>();
}


