package ge.OCMS.repository;

import ge.OCMS.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();
    }

    @Test
    @Rollback(value = false)
    void whenFindByRoleName_existingRole_thenReturnRole() {

        Role role = new Role();
        role.setRoleName("STUDENT");
        roleRepository.save(role);

        Optional<Role> foundRole = roleRepository.findByRoleName("STUDENT");

        assertThat(foundRole).isPresent();
        assertThat(foundRole.get().getRoleName()).isEqualTo("STUDENT");
    }

    @Test
    void whenFindByRoleName_nonExistingRole_thenReturnEmpty() {

        Optional<Role> foundRole = roleRepository.findByRoleName("NON_EXISTING_ROLE");

        assertThat(foundRole).isNotPresent();
    }
}
