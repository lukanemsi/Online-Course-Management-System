package ge.OCMS.service;

import ge.OCMS.entity.Course;
import ge.OCMS.entity.Lesson;
import ge.OCMS.exception.custom.EntityNotFoundException;
import ge.OCMS.repository.CourseRepository;
import ge.OCMS.repository.LessonRepository;
import ge.OCMS.util.JsonConverter;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;

    private final CourseRepository courseRepository;

    private final EntityManager entityManager;

    public ResponseEntity<List<Lesson>> getAllLessons() {
        return ResponseEntity.ok(lessonRepository.findAll());
    }

    public ResponseEntity<Lesson> getLessonById(Long id) {
        Optional<Lesson> lesson = lessonRepository.findById(id);
        return lesson.map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with given id: " + id));
    }

    @Transactional
    public ResponseEntity<Lesson> createLesson(Lesson lesson) {
        Lesson savedLesson = lessonRepository.save(lesson);
        lessonRepository.flush();
        entityManager.refresh(savedLesson);
        log.trace("Add Lesson: {}", JsonConverter.toJson(savedLesson));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLesson);
    }

    @Transactional
    public ResponseEntity<Void> updateLesson(Long id, Lesson lesson) {
        if (!lessonRepository.existsById(id))
            throw new EntityNotFoundException("Entity not found with given id: " + id);
        lesson.setLessonId(id);
        lessonRepository.save(lesson);
        log.trace("Update Lesson: {}", JsonConverter.toJson(lesson));
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<Void> deleteLesson(Long id) {
        if (lessonRepository.existsById(id)) {
            lessonRepository.deleteById(id);
            log.trace("Delete Lesson with id: {}", id);
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<Lesson>> findByCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Course not found with given id: " + id)
        );
        return ResponseEntity.ok(lessonRepository.findByCourse(course));
    }

    public ResponseEntity<Set<Lesson>> searchLessons(String topic,String content){
        return ResponseEntity.ok(lessonRepository.searchLesson(topic,content));
    }
}
