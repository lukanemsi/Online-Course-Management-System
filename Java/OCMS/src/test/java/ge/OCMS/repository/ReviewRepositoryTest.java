package ge.OCMS.repository;

import ge.OCMS.entity.Course;
import ge.OCMS.entity.CourseCategory;
import ge.OCMS.entity.Review;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseCategoryRepository courseCategoryRepository;

    private Course course;

    private CourseCategory courseCategory;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        courseRepository.deleteAll();
        courseCategoryRepository.deleteAll();

        courseCategory = new CourseCategory();
        courseCategory.setCategoryName("Science");
        courseCategory = courseCategoryRepository.save(courseCategory);

        course = new Course();
        course.setTitle("Math 101");
        course.setCategory(courseCategory);
        course = courseRepository.save(course);

        Review review1 = new Review();
        review1.setStudentName("student1");
        review1.setComment("Great course!");
        review1.setRating(5);
        review1.setCourse(course);
        reviewRepository.save(review1);

        Review review2 = new Review();
        review2.setStudentName("student2");
        review2.setComment("Good content");
        review2.setRating(4);
        review2.setCourse(course);
        reviewRepository.save(review2);
    }

    @Test
    void whenFindByCourse_existingCourse_thenReturnReviews() {
        List<Review> reviews = reviewRepository.findByCourse(course);

        assertThat(reviews).hasSize(2);
        assertThat(reviews.get(0).getCourse()).isEqualTo(course);
        assertThat(reviews.get(1).getCourse()).isEqualTo(course);
    }

    @Test
    void whenFindByCourse_nonExistingCourse_thenReturnEmptyList() {
        Course nonExistingCourse = new Course();
        nonExistingCourse.setTitle("History 101");
        nonExistingCourse.setCategory(courseCategoryRepository.findById(courseCategory.getCategoryId()).get());
        courseRepository.save(nonExistingCourse);

        List<Review> reviews = reviewRepository.findByCourse(nonExistingCourse);

        assertThat(reviews).isEmpty();
    }

    @Test
    void whenSearchReview_withMatchingCriteria_thenReturnFilteredResults() {
        Set<Review> reviews = reviewRepository.searchReview("student1", "Great", 5);

        assertThat(reviews).hasSize(1);
        assertThat(reviews.iterator().next().getStudentName()).isEqualTo("student1");
    }

    @Test
    void whenSearchReview_withPartialCriteria_thenReturnAllMatchingResults() {
        Set<Review> reviews = reviewRepository.searchReview(null, "Good", null);

        assertThat(reviews).hasSize(1);
        assertThat(reviews.iterator().next().getComment()).contains("Good");
    }

    @Test
    void whenSearchReview_withNonMatchingCriteria_thenReturnEmptySet() {
        Set<Review> reviews = reviewRepository.searchReview("nonexistentUser", "Bad", 1);

        assertThat(reviews).isEmpty();
    }
}
