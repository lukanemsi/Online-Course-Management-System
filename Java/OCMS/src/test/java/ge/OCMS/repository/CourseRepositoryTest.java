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
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseCategoryRepository courseCategoryRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    private CourseCategory programmingCategory;
    private Course javaCourse;
    private Course pythonCourse;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
        courseRepository.deleteAll();
        courseCategoryRepository.deleteAll();

        programmingCategory = new CourseCategory();
        programmingCategory.setCategoryName("Programming");
        programmingCategory = courseCategoryRepository.save(programmingCategory);

        javaCourse = new Course();
        javaCourse.setTitle("Java Basics");
        javaCourse.setDescription("Learn Java from scratch.");
        javaCourse.setCategory(programmingCategory);
        courseRepository.save(javaCourse);

        pythonCourse = new Course();
        pythonCourse.setTitle("Python Basics");
        pythonCourse.setDescription("Learn Python from scratch.");
        pythonCourse.setCategory(programmingCategory);
        courseRepository.save(pythonCourse);

        Review review1 = new Review();
        review1.setCourse(javaCourse);
        review1.setRating(5);
        reviewRepository.save(review1);

        Review review2 = new Review();
        review2.setCourse(javaCourse);
        review2.setRating(4);
        reviewRepository.save(review2);

        Review review3 = new Review();
        review3.setCourse(pythonCourse);
        review3.setRating(3);
        reviewRepository.save(review3);
    }

    @Test
    void whenSearchCourses_withCategoryNameAndTitle_thenReturnMatchingCourses() {
        Set<Course> courses = courseRepository.searchCourses("Programming", "Java Basics", null);

        assertThat(courses).hasSize(1);
        assertThat(courses.iterator().next()).isEqualTo(javaCourse);
    }

    @Test
    void whenSearchCourses_withDescription_thenReturnMatchingCourses() {
        Set<Course> courses = courseRepository.searchCourses(null, null, "Learn Python from scratch.");

        assertThat(courses).hasSize(1);
        assertThat(courses.iterator().next()).isEqualTo(pythonCourse);
    }

    @Test
    void whenSearchCourses_withNoFilters_thenReturnAllCourses() {
        Set<Course> courses = courseRepository.searchCourses(null, null, null);

        assertThat(courses).hasSize(2);
        assertThat(courses).contains(javaCourse, pythonCourse);
    }

    @Test
    void whenFindCourseWithRatings_thenReturnCoursesOrderedByAverageRating() {
        List<Course> coursesWithRatings = courseRepository.findCourseWithRatings();

        assertThat(coursesWithRatings).hasSize(2);
        assertThat(coursesWithRatings.get(0)).isEqualTo(javaCourse);
        assertThat(coursesWithRatings.get(1)).isEqualTo(pythonCourse);
    }
}
