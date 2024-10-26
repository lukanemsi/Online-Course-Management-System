package ge.OCMS.controller;

import ge.OCMS.entity.Lesson;
import ge.OCMS.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @GetMapping()
    public ResponseEntity<List<Lesson>> getAllLessons() {
        return lessonService.getAllLessons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long id) {
        return lessonService.getLessonById(id);
    }

    @PostMapping()
    public ResponseEntity<Lesson> createLesson(@RequestBody Lesson lesson) {
        return lessonService.createLesson(lesson);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLesson(@PathVariable Long id, @RequestBody Lesson lesson) {
        return lessonService.updateLesson(id, lesson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        return lessonService.deleteLesson(id);
    }

    @GetMapping("/search/course/{id}")
    public ResponseEntity<List<Lesson>> findByCourse(@PathVariable Long id) {
        return lessonService.findByCourse(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Set<Lesson>> searchCourses(@RequestParam(required = false) String topic, @RequestParam(required = false) String content) {
        return lessonService.searchLessons(topic, content);
    }

}