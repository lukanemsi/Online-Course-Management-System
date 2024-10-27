package ge.OCMS.service;

import ge.OCMS.entity.Course;
import ge.OCMS.entity.Lesson;
import ge.OCMS.exception.custom.EntityNotFoundException;
import ge.OCMS.repository.CourseRepository;
import ge.OCMS.repository.LessonRepository;
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

public class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private LessonService lessonService;

    private Lesson sampleLesson;
    private Course sampleCourse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleCourse = new Course();
        sampleCourse.setCourseId(1L);
        sampleCourse.setTitle("Sample Course");

        sampleLesson = new Lesson();
        sampleLesson.setLessonId(1L);
        sampleLesson.setTopic("Sample Lesson");
        sampleLesson.setCourse(sampleCourse);
    }

    @Test
    void whenGetAllLessons_thenReturnAllLessons() {
        when(lessonRepository.findAll()).thenReturn(List.of(sampleLesson));

        ResponseEntity<List<Lesson>> response = lessonService.getAllLessons();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(sampleLesson);
    }

    @Test
    void whenGetLessonById_withValidId_thenReturnLesson() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.of(sampleLesson));

        ResponseEntity<Lesson> response = lessonService.getLessonById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sampleLesson);
    }

    @Test
    void whenGetLessonById_withInvalidId_thenThrowException() {
        when(lessonRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.getLessonById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Entity not found with given id: 1");
    }

    @Test
    void whenCreateLesson_thenReturnCreatedLesson() {
        when(lessonRepository.save(sampleLesson)).thenReturn(sampleLesson);

        ResponseEntity<Lesson> response = lessonService.createLesson(sampleLesson);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(sampleLesson);
        verify(lessonRepository).flush();
        verify(entityManager).refresh(sampleLesson);
    }

    @Test
    void whenUpdateLesson_withValidId_thenUpdateLesson() {
        when(lessonRepository.existsById(1L)).thenReturn(true);

        ResponseEntity<Void> response = lessonService.updateLesson(1L, sampleLesson);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(lessonRepository).save(sampleLesson);
    }

    @Test
    void whenUpdateLesson_withInvalidId_thenThrowException() {
        when(lessonRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> lessonService.updateLesson(1L, sampleLesson))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Entity not found with given id: 1");
    }

    @Test
    void whenDeleteLesson_withValidId_thenDeleteLesson() {
        when(lessonRepository.existsById(1L)).thenReturn(true);

        ResponseEntity<Void> response = lessonService.deleteLesson(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(lessonRepository).deleteById(1L);
    }

    @Test
    void whenFindByCourse_withValidCourseId_thenReturnLessons() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(sampleCourse));
        when(lessonRepository.findByCourse(sampleCourse)).thenReturn(List.of(sampleLesson));

        ResponseEntity<List<Lesson>> response = lessonService.findByCourse(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(sampleLesson);
    }

    @Test
    void whenFindByCourse_withInvalidCourseId_thenThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.findByCourse(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Course not found with given id: 1");
    }

    @Test
    void whenSearchLessons_thenReturnMatchingLessons() {
        when(lessonRepository.searchLesson("Sample Topic", "Sample Content"))
                .thenReturn(Set.of(sampleLesson));

        ResponseEntity<Set<Lesson>> response = lessonService.searchLessons("Sample Topic", "Sample Content");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(sampleLesson);
    }
}
