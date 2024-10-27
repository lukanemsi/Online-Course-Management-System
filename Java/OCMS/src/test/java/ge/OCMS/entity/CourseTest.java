package ge.OCMS.entity;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CourseTest {

    @Test
    void getCourseId_whenSetCourseId_thenReturnsCourseId() {
        Course course = new Course();
        Long expectedCourseId = 1L;

        course.setCourseId(expectedCourseId);
        Long actualCourseId = course.getCourseId();

        assertEquals(expectedCourseId, actualCourseId);
    }

    @Test
    void getTitle_whenSetTitle_thenReturnsTitle() {
        Course course = new Course();
        String expectedTitle = "Java Programming";

        course.setTitle(expectedTitle);
        String actualTitle = course.getTitle();

        assertEquals(expectedTitle, actualTitle);
    }

    @Test
    void getDescription_whenSetDescription_thenReturnsDescription() {
        Course course = new Course();
        String expectedDescription = "An in-depth look at Java programming.";

        course.setDescription(expectedDescription);
        String actualDescription = course.getDescription();

        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    void getCategory_whenSetCategory_thenReturnsCategory() {
        Course course = new Course();
        CourseCategory expectedCategory = new CourseCategory();
        expectedCategory.setCategoryId(1L);

        course.setCategory(expectedCategory);
        CourseCategory actualCategory = course.getCategory();

        assertEquals(expectedCategory, actualCategory);
    }

    @Test
    void getLessons_whenSetLessons_thenReturnsLessons() {
        Course course = new Course();
        Lesson lesson = new Lesson();
        lesson.setLessonId(1L);

        Set<Lesson> expectedLessons = new HashSet<>();
        expectedLessons.add(lesson);
        course.setLessons(expectedLessons);

        Set<Lesson> actualLessons = course.getLessons();

        assertEquals(expectedLessons, actualLessons);
    }

    @Test
    void equals_whenSameId_thenReturnsTrue() {
        Course course1 = new Course();
        course1.setCourseId(1L);

        Course course2 = new Course();
        course2.setCourseId(1L);

        assertEquals(course1, course2);
    }

    @Test
    void equals_whenDifferentId_thenReturnsFalse() {
        Course course1 = new Course();
        course1.setCourseId(1L);

        Course course2 = new Course();
        course2.setCourseId(2L);

        assertNotEquals(course1, course2);
    }

    @Test
    void equals_whenDifferentClass_thenReturnsFalse() {
        Course course = new Course();
        course.setCourseId(1L);

        String differentClass = "Not a Course";

        assertNotEquals(course, differentClass);
    }

    @Test
    void hashCode_whenSameId_thenReturnsSameHashCode() {
        Course course1 = new Course();
        course1.setCourseId(1L);

        Course course2 = new Course();
        course2.setCourseId(1L);

        assertEquals(course1.hashCode(), course2.hashCode());
    }

    @Test
    void hashCode_whenDifferentId_thenReturnsDifferentHashCode() {
        Course course1 = new Course();
        course1.setCourseId(1L);

        Course course2 = new Course();
        course2.setCourseId(2L);

        assertNotEquals(course1.hashCode(), course2.hashCode());
    }
}
