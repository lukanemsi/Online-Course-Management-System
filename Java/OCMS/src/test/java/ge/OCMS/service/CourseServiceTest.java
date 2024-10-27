package ge.OCMS.service;

import ge.OCMS.entity.Course;
import ge.OCMS.exception.custom.EntityNotFoundException;
import ge.OCMS.exception.custom.InvalidRequestException;
import ge.OCMS.repository.CourseRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EntityManager entityManager;

    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        course = new Course();
        course.setCourseId(1L);
    }

    @Test
    void getAllCourses_ReturnsListOfCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(course);

        when(courseRepository.findAll()).thenReturn(courses);

        ResponseEntity<List<Course>> response = courseService.getAllCourses();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(courses, response.getBody());
    }

    @Test
    void getCourseById_ExistingId_ReturnsCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        ResponseEntity<Course> response = courseService.getCourseById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(course, response.getBody());
    }

    @Test
    void getCourseById_NonExistingId_ThrowsEntityNotFoundException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            courseService.getCourseById(1L);
        });

        assertEquals("Entity not found with given id: 1", exception.getMessage());
    }

    @Test
    void createCourse_SavesCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        ResponseEntity<Course> response = courseService.createCourse(course);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(course, response.getBody());
        verify(courseRepository).save(course);
        verify(courseRepository).flush();
        verify(entityManager).refresh(course);
    }

    @Test
    public void updateCourse_ExistingId_UpdatesCourse() {
        Course existingCourse = new Course();
        existingCourse.setCourseId(1L);
        existingCourse.setTitle("Existing Course");
        when(courseRepository.existsById(1L)).thenReturn(true);

        courseService.updateCourse(existingCourse.getCourseId(), existingCourse);

        verify(courseRepository, times(1)).save(existingCourse);
    }

    @Test
    void updateCourse_NonExistingId_ThrowsEntityNotFoundException() {
        when(courseRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            courseService.updateCourse(1L, course);
        });

        assertEquals("Entity not found with given id: 1", exception.getMessage());
    }

    @Test
    void deleteCourse_ExistingId_DeletesCourse() {
        when(courseRepository.existsById(1L)).thenReturn(true);

        ResponseEntity<Void> response = courseService.deleteCourse(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(courseRepository).deleteById(1L);
    }

    @Test
    void deleteCourse_NonExistingId_DoesNotThrow() {
        when(courseRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<Void> response = courseService.deleteCourse(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(courseRepository, never()).deleteById(1L);
    }

    @Test
    void ratingCourses_WithFirstOrder_ReturnsFirstCourse() {
        List<Course> courses = List.of(course, new Course());

        when(courseRepository.findCourseWithRatings()).thenReturn(courses);

        ResponseEntity<Course> response = courseService.ratingCourses("first");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(course, response.getBody());
    }

    @Test
    void ratingCourses_WithLastOrder_ReturnsLastCourse() {
        Course lastCourse = new Course();
        List<Course> courses = List.of(course, lastCourse);

        when(courseRepository.findCourseWithRatings()).thenReturn(courses);

        ResponseEntity<Course> response = courseService.ratingCourses("last");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(lastCourse, response.getBody());
    }

    @Test
    void ratingCourses_WithInvalidOrder_ThrowsInvalidRequestException() {
        when(courseRepository.findCourseWithRatings()).thenReturn(List.of(new Course()));

        Exception exception = assertThrows(InvalidRequestException.class, () -> {
            courseService.ratingCourses("invalid");
        });

        assertEquals("Order Path variable can only be 'first' or 'last'", exception.getMessage());
    }

    @Test
    void searchCourses_ReturnsSetOfCourses() {
        Set<Course> courseSet = new HashSet<>();
        courseSet.add(course);

        when(courseRepository.searchCourses("category", "title", "description")).thenReturn(courseSet);

        ResponseEntity<Set<Course>> response = courseService.searchCourses("category", "title", "description");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(courseSet, response.getBody());
    }
}
