package ge.OCMS.repository;

import ge.OCMS.entity.Course;
import ge.OCMS.entity.CourseCategory;
import ge.OCMS.entity.Enrollment;
import ge.OCMS.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseCategoryRepository courseCategoryRepository;

    private User user1;
    private User user2;
    private Course course1;
    private Course course2;

    @BeforeEach
    void setUp() {
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
        courseCategoryRepository.deleteAll();

        CourseCategory programmingCategory = new CourseCategory();
        programmingCategory.setCategoryName("Programming");
        courseCategoryRepository.save(programmingCategory);

        course1 = new Course();
        course1.setTitle("Java Basics");
        course1.setCategory(programmingCategory);
        course1 = courseRepository.save(course1);

        course2 = new Course();
        course2.setTitle("Python Basics");
        course2.setCategory(programmingCategory);
        course2 = courseRepository.save(course2);

        user1 = new User();
        user1.setUsername("student01");
        user1.setPassword("pass");
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setUsername("student02");
        user2.setPassword("pass");
        user2 = userRepository.save(user2);

        Enrollment enrollment1 = new Enrollment();
        enrollment1.setUser(user1);
        enrollment1.setCourse(course1);
        enrollmentRepository.save(enrollment1);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setUser(user2);
        enrollment2.setCourse(course1);
        enrollmentRepository.save(enrollment2);

        Enrollment enrollment3 = new Enrollment();
        enrollment3.setUser(user1);
        enrollment3.setCourse(course2);
        enrollmentRepository.save(enrollment3);
    }

    @Test
    void whenFindByUser_thenReturnEnrollmentsForUser() {
        List<Enrollment> enrollmentsForUser1 = enrollmentRepository.findByUser(user1);

        assertThat(enrollmentsForUser1).hasSize(2);
        assertThat(enrollmentsForUser1).allMatch(enrollment -> enrollment.getUser().equals(user1));
    }

    @Test
    void whenFindByUser_withNoEnrollments_thenReturnEmptyList() {
        User newUser = new User();
        newUser.setUsername("student03");
        newUser.setPassword("pass");
        userRepository.save(newUser);

        List<Enrollment> enrollments = enrollmentRepository.findByUser(newUser);

        assertThat(enrollments).isEmpty();
    }

    @Test
    void whenFindByCourse_thenReturnEnrollmentsForCourse() {
        List<Enrollment> enrollmentsForCourse1 = enrollmentRepository.findByCourse(course1);

        assertThat(enrollmentsForCourse1).hasSize(2);
        assertThat(enrollmentsForCourse1).allMatch(enrollment -> enrollment.getCourse().equals(course1));
    }

    @Test
    void whenFindByCourse_withNoEnrollments_thenReturnEmptyList() {
        Course newCourse = new Course();
        newCourse.setTitle("Ruby Basics");
        newCourse.setCategory(courseCategoryRepository.findById(course1.getCategory().getCategoryId()).orElse(null));
        courseRepository.save(newCourse);

        List<Enrollment> enrollments = enrollmentRepository.findByCourse(newCourse);

        assertThat(enrollments).isEmpty();
    }
}
