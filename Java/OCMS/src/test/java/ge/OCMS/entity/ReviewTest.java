package ge.OCMS.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ReviewTest {

    @Test
    void getId_whenSetId_thenReturnsId() {
        Review review = new Review();
        Long expectedId = 1L;

        review.setId(expectedId);
        Long actualId = review.getId();

        assertEquals(expectedId, actualId);
    }

    @Test
    void getStudentName_whenSetStudentName_thenReturnsStudentName() {
        Review review = new Review();
        String expectedStudentName = "John Doe";

        review.setStudentName(expectedStudentName);
        String actualStudentName = review.getStudentName();

        assertEquals(expectedStudentName, actualStudentName);
    }

    @Test
    void getComment_whenSetComment_thenReturnsComment() {
        Review review = new Review();
        String expectedComment = "Great course!";

        review.setComment(expectedComment);
        String actualComment = review.getComment();

        assertEquals(expectedComment, actualComment);
    }

    @Test
    void getRating_whenSetRating_thenReturnsRating() {
        Review review = new Review();
        int expectedRating = 5;

        review.setRating(expectedRating);
        int actualRating = review.getRating();

        assertEquals(expectedRating, actualRating);
    }

    @Test
    void getCourse_whenSetCourse_thenReturnsCourse() {
        Review review = new Review();
        Course expectedCourse = new Course();
        expectedCourse.setCourseId(1L);

        review.setCourse(expectedCourse);
        Course actualCourse = review.getCourse();

        assertEquals(expectedCourse, actualCourse);
    }

    @Test
    void equals_whenSameId_thenReturnsTrue() {
        Review review1 = new Review();
        review1.setId(1L);

        Review review2 = new Review();
        review2.setId(1L);

        assertEquals(review1, review2);
    }

    @Test
    void equals_whenDifferentId_thenReturnsFalse() {
        Review review1 = new Review();
        review1.setId(1L);

        Review review2 = new Review();
        review2.setId(2L);

        assertNotEquals(review1, review2);
    }

    @Test
    void hashCode_whenSameId_thenReturnsSameHashCode() {
        Review review1 = new Review();
        review1.setId(1L);

        Review review2 = new Review();
        review2.setId(1L);

        assertEquals(review1.hashCode(), review2.hashCode());
    }

    @Test
    void hashCode_whenDifferentId_thenReturnsDifferentHashCode() {
        Review review1 = new Review();
        review1.setId(1L);

        Review review2 = new Review();
        review2.setId(2L);

        assertNotEquals(review1.hashCode(), review2.hashCode());
    }
}
