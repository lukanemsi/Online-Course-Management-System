package ge.OCMS.service;

import ge.OCMS.entity.Course;
import ge.OCMS.entity.Enrollment;
import ge.OCMS.entity.Role;
import ge.OCMS.entity.User;
import ge.OCMS.exception.custom.EntityNotFoundException;
import ge.OCMS.exception.custom.InvalidRequestException;
import ge.OCMS.repository.CourseRepository;
import ge.OCMS.repository.EnrollmentRepository;
import ge.OCMS.repository.UserRepository;
import ge.OCMS.util.JsonConverter;
import ge.OCMS.wrapper.RoleEnum;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;


    private final EntityManager entityManager;

    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentRepository.findAll());
    }

    public ResponseEntity<Enrollment> getEnrollmentById(Long id) {
        Optional<Enrollment> enrollment = enrollmentRepository.findById(id);
        return enrollment.map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with given id: " + id));
    }

    @Transactional
    public ResponseEntity<Enrollment> createEnrollment(Enrollment enrollment) {
        validateEnrollmentUser(enrollment.getUser());
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        enrollmentRepository.flush();
        entityManager.refresh(savedEnrollment);
        log.trace("Add Enrollment: {}", JsonConverter.toJson(savedEnrollment));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEnrollment);
    }

    @Transactional
    public ResponseEntity<Void> updateEnrollment(Long id, Enrollment enrollment) {
        if (!enrollmentRepository.existsById(id))
            throw new EntityNotFoundException("Entity not found with given id: " + id);

        enrollment.setId(id);
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        log.trace("Update Enrollment: {}", JsonConverter.toJson(updatedEnrollment));
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<Void> deleteEnrollment(Long id) {
        if (enrollmentRepository.existsById(id)) {
            enrollmentRepository.deleteById(id);
            log.trace("Delete Enrollment with id: {}", id);
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<Enrollment>> getEnrollmentByUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found with given id: " + id));
        return ResponseEntity.ok(enrollmentRepository.findByUser(user));
    }

    public ResponseEntity<List<Enrollment>> getEnrollmentByCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Course not found with given id: " + id)
        );
        return ResponseEntity.ok(enrollmentRepository.findByCourse(course));
    }
    private void validateEnrollmentUser(User userInput) {
        User user = userRepository.findById(userInput.getId())
                .orElseThrow(() -> new EntityNotFoundException("Can't enroll in course User does not exists"));
        entityManager.refresh(user);
        boolean matches =
                user.getRoles().stream()
                        .map(Role::getRoleName)
                        .anyMatch(r -> RoleEnum.STUDENT.getValue().equals(r));
        if (!matches) {
            throw new InvalidRequestException("User is not student, can not enroll in course");
        }
    }
}
