package ge.OCMS.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EnrollmentTest {

    @Test
    void getId_whenSetId_thenReturnsId() {
        Enrollment enrollment = new Enrollment();
        Long expectedId = 1L;

        enrollment.setId(expectedId);
        Long actualId = enrollment.getId();

        assertEquals(expectedId, actualId);
    }

    @Test
    void getActive_whenSetActive_thenReturnsActive() {
        Enrollment enrollment = new Enrollment();
        Boolean expectedActive = false;

        enrollment.setActive(expectedActive);
        Boolean actualActive = enrollment.getActive();

        assertEquals(expectedActive, actualActive);
    }

    @Test
    void getCourse_whenSetCourse_thenReturnsCourse() {
        Enrollment enrollment = new Enrollment();
        Course expectedCourse = new Course();
        expectedCourse.setCourseId(1L);

        enrollment.setCourse(expectedCourse);
        Course actualCourse = enrollment.getCourse();

        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void getUser_whenSetUser_thenReturnsUser() {
        Enrollment enrollment = new Enrollment();
        User expectedUser = new User();
        expectedUser.setId(1L);

        enrollment.setUser(expectedUser);
        User actualUser = enrollment.getUser();

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void equals_whenSameId_thenReturnsTrue() {
        Enrollment enrollment1 = new Enrollment();
        enrollment1.setId(1L);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setId(1L);

        assertEquals(enrollment1, enrollment2);
    }

    @Test
    void equals_whenDifferentId_thenReturnsFalse() {
        Enrollment enrollment1 = new Enrollment();
        enrollment1.setId(1L);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setId(2L);

        assertNotEquals(enrollment1, enrollment2);
    }

    @Test
    void equals_whenDifferentClass_thenReturnsFalse() {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);

        String differentClass = "Not an Enrollment";

        assertNotEquals(enrollment, differentClass);
    }

    @Test
    void hashCode_whenSameId_thenReturnsSameHashCode() {
        Enrollment enrollment1 = new Enrollment();
        enrollment1.setId(1L);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setId(1L);

        assertEquals(enrollment1.hashCode(), enrollment2.hashCode());
    }

    @Test
    void hashCode_whenDifferentId_thenReturnsDifferentHashCode() {
        Enrollment enrollment1 = new Enrollment();
        enrollment1.setId(1L);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setId(2L);

        assertNotEquals(enrollment1.hashCode(), enrollment2.hashCode());
    }
}
