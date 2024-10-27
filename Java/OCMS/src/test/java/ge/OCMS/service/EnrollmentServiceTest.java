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
import ge.OCMS.wrapper.RoleEnum;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private Enrollment sampleEnrollment;
    private User sampleUser;
    private Course sampleCourse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleCourse = new Course();
        sampleCourse.setCourseId(1L);
        sampleCourse.setTitle("Sample Course");

        Role role = new Role();
        role.setRoleName(RoleEnum.STUDENT.getValue());
        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setUsername("testUser");
        sampleUser.setRoles(Set.of(role));

        sampleEnrollment = new Enrollment();
        sampleEnrollment.setId(1L);
        sampleEnrollment.setUser(sampleUser);
        sampleEnrollment.setCourse(sampleCourse);
    }

    @Test
    void whenGetAllEnrollments_thenReturnAllEnrollments() {
        when(enrollmentRepository.findAll()).thenReturn(List.of(sampleEnrollment));

        ResponseEntity<List<Enrollment>> response = enrollmentService.getAllEnrollments();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(sampleEnrollment);
    }

    @Test
    void whenGetEnrollmentById_withValidId_thenReturnEnrollment() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(sampleEnrollment));

        ResponseEntity<Enrollment> response = enrollmentService.getEnrollmentById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sampleEnrollment);
    }

    @Test
    void whenGetEnrollmentById_withInvalidId_thenThrowException() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.getEnrollmentById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Entity not found with given id: 1");
    }

    @Test
    void whenCreateEnrollment_thenReturnCreatedEnrollment() {
        when(enrollmentRepository.save(sampleEnrollment)).thenReturn(sampleEnrollment);
        when(userRepository.findById(sampleUser.getId())).thenReturn(Optional.of(sampleUser));

        ResponseEntity<Enrollment> response = enrollmentService.createEnrollment(sampleEnrollment);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(sampleEnrollment);
        verify(enrollmentRepository).flush();
        verify(entityManager).refresh(sampleEnrollment);
    }

    @Test
    void whenCreateEnrollment_withNonStudentUser_thenThrowException() {
        sampleUser.setRoles(Set.of(new Role()));
        when(userRepository.findById(sampleUser.getId())).thenReturn(Optional.of(sampleUser));

        assertThatThrownBy(() -> enrollmentService.createEnrollment(sampleEnrollment))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessageContaining("User is not student, can not enroll in course");
    }

    @Test
    void whenUpdateEnrollment_withValidId_thenUpdateEnrollment() {
        when(enrollmentRepository.existsById(1L)).thenReturn(true);
        sampleEnrollment.setId(1L);

        ResponseEntity<Void> response = enrollmentService.updateEnrollment(1L, sampleEnrollment);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(enrollmentRepository).save(sampleEnrollment);
    }

    @Test
    void whenUpdateEnrollment_withInvalidId_thenThrowException() {
        when(enrollmentRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> enrollmentService.updateEnrollment(1L, sampleEnrollment))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Entity not found with given id: 1");
    }

    @Test
    void whenDeleteEnrollment_withValidId_thenDeleteEnrollment() {
        when(enrollmentRepository.existsById(1L)).thenReturn(true);

        ResponseEntity<Void> response = enrollmentService.deleteEnrollment(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(enrollmentRepository).deleteById(1L);
    }

    @Test
    void whenGetEnrollmentByUser_withValidUserId_thenReturnEnrollments() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(enrollmentRepository.findByUser(sampleUser)).thenReturn(List.of(sampleEnrollment));

        ResponseEntity<List<Enrollment>> response = enrollmentService.getEnrollmentByUser(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(sampleEnrollment);
    }

    @Test
    void whenGetEnrollmentByUser_withInvalidUserId_thenThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.getEnrollmentByUser(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found with given id: 1");
    }

    @Test
    void whenGetEnrollmentByCourse_withValidCourseId_thenReturnEnrollments() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(sampleCourse));
        when(enrollmentRepository.findByCourse(sampleCourse)).thenReturn(List.of(sampleEnrollment));

        ResponseEntity<List<Enrollment>> response = enrollmentService.getEnrollmentByCourse(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(sampleEnrollment);
    }

    @Test
    void whenGetEnrollmentByCourse_withInvalidCourseId_thenThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> enrollmentService.getEnrollmentByCourse(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Course not found with given id: 1");
    }
}
