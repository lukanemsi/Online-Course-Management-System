package ge.OCMS.repository;

import ge.OCMS.entity.Course;
import ge.OCMS.entity.CourseCategory;
import ge.OCMS.entity.Lesson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class LessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseCategoryRepository courseCategoryRepository;

    private Course course;

    private CourseCategory courseCategory;

    @BeforeEach
    void setUp() {
        lessonRepository.deleteAll();
        courseRepository.deleteAll();
        courseCategoryRepository.deleteAll();

        courseCategory = new CourseCategory();
        courseCategory.setCategoryName("Programming");
        courseCategory = courseCategoryRepository.save(courseCategory);

        course = new Course();
        course.setTitle("Java Basics");
        course.setCategory(courseCategory);
        course = courseRepository.save(course);

        Lesson lesson1 = new Lesson();
        lesson1.setTopic("Introduction to Java");
        lesson1.setContent("Java basics and setup");
        lesson1.setCourse(course);
        lessonRepository.save(lesson1);

        Lesson lesson2 = new Lesson();
        lesson2.setTopic("Java Variables");
        lesson2.setContent("Understanding Java variables");
        lesson2.setCourse(course);
        lessonRepository.save(lesson2);
    }

    @Test
    void whenFindByCourse_existingCourse_thenReturnLessons() {
        List<Lesson> lessons = lessonRepository.findByCourse(course);

        assertThat(lessons).hasSize(2);
        assertThat(lessons.get(0).getCourse()).isEqualTo(course);
        assertThat(lessons.get(1).getCourse()).isEqualTo(course);
    }

    @Test
    void whenFindByCourse_nonExistingCourse_thenReturnEmptyList() {
        Course nonExistingCourse = new Course();
        nonExistingCourse.setCategory(courseCategoryRepository.findById(courseCategory.getCategoryId()).get());
        nonExistingCourse.setTitle("Python Basics");
        courseRepository.save(nonExistingCourse);

        List<Lesson> lessons = lessonRepository.findByCourse(nonExistingCourse);

        assertThat(lessons).isEmpty();
    }

    @Test
    void whenSearchLesson_withMatchingCriteria_thenReturnFilteredResults() {
        Set<Lesson> lessons = lessonRepository.searchLesson("Java Variables", "variables");

        assertThat(lessons).hasSize(1);
        assertThat(lessons.iterator().next().getTopic()).isEqualTo("Java Variables");
    }

    @Test
    void whenSearchLesson_withPartialCriteria_thenReturnAllMatchingResults() {
        Set<Lesson> lessons = lessonRepository.searchLesson(null, "Java");

        assertThat(lessons).hasSize(2);
        assertThat(lessons.iterator().next().getContent()).contains("Java");
    }

    @Test
    void whenSearchLesson_withNonMatchingCriteria_thenReturnEmptySet() {
        Set<Lesson> lessons = lessonRepository.searchLesson("Nonexistent Topic", "Nonexistent Content");

        assertThat(lessons).isEmpty();
    }
}
