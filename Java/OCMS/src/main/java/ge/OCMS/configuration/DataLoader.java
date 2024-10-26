package ge.OCMS.configuration;

import ge.OCMS.entity.Role;
import ge.OCMS.repository.RoleRepository;
import ge.OCMS.wrapper.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// NOTE: this class is only for testing porpoises, it is not needed for production env
@Log4j2
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        Role studentRole = new Role();
        Role instructorRole = new Role();

        studentRole.setRoleName(RoleEnum.STUDENT.getValue());
        instructorRole.setRoleName(RoleEnum.INSTRUCTOR.getValue());
        roleRepository.save(studentRole);
        roleRepository.save(instructorRole);
        log.info("roles inserted");
    }
}
