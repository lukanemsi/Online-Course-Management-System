package ge.OCMS.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class LessonTest {

    @Test
    void getLessonId_whenSetLessonId_thenReturnsLessonId() {
        Lesson lesson = new Lesson();
        Long expectedLessonId = 1L;

        lesson.setLessonId(expectedLessonId);
        Long actualLessonId = lesson.getLessonId();

        assertEquals(expectedLessonId, actualLessonId);
    }

    @Test
    void getTopic_whenSetTopic_thenReturnsTopic() {
        Lesson lesson = new Lesson();
        String expectedTopic = "Introduction to Java";

        lesson.setTopic(expectedTopic);
        String actualTopic = lesson.getTopic();

        assertEquals(expectedTopic, actualTopic);
    }

    @Test
    void getContent_whenSetContent_thenReturnsContent() {
        Lesson lesson = new Lesson();
        String expectedContent = "This lesson covers the basics of Java.";

        lesson.setContent(expectedContent);
        String actualContent = lesson.getContent();

        assertEquals(expectedContent, actualContent);
    }

    @Test
    void getCourse_whenSetCourse_thenReturnsCourse() {
        Lesson lesson = new Lesson();
        Course expectedCourse = new Course();
        expectedCourse.setCourseId(1L);

        lesson.setCourse(expectedCourse);
        Course actualCourse = lesson.getCourse();

        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void equals_whenSameLessonId_thenReturnsTrue() {
        Lesson lesson1 = new Lesson();
        lesson1.setLessonId(1L);

        Lesson lesson2 = new Lesson();
        lesson2.setLessonId(1L);

        assertEquals(lesson1, lesson2);
    }

    @Test
    void equals_whenDifferentLessonId_thenReturnsFalse() {
        Lesson lesson1 = new Lesson();
        lesson1.setLessonId(1L);

        Lesson lesson2 = new Lesson();
        lesson2.setLessonId(2L);

        assertNotEquals(lesson1, lesson2);
    }

    @Test
    void equals_whenDifferentClass_thenReturnsFalse() {
        Lesson lesson = new Lesson();
        lesson.setLessonId(1L);

        String differentClass = "Not a Lesson";

        assertNotEquals(lesson, differentClass);
    }

    @Test
    void hashCode_whenSameLessonId_thenReturnsSameHashCode() {
        Lesson lesson1 = new Lesson();
        lesson1.setLessonId(1L);

        Lesson lesson2 = new Lesson();
        lesson2.setLessonId(1L);

        assertEquals(lesson1.hashCode(), lesson2.hashCode());
    }

    @Test
    void hashCode_whenDifferentLessonId_thenReturnsDifferentHashCode() {
        Lesson lesson1 = new Lesson();
        lesson1.setLessonId(1L);

        Lesson lesson2 = new Lesson();
        lesson2.setLessonId(2L);

        assertNotEquals(lesson1.hashCode(), lesson2.hashCode());
    }
}
