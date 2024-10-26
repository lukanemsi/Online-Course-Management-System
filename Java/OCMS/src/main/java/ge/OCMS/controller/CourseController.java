package ge.OCMS.controller;


import ge.OCMS.entity.Course;
import ge.OCMS.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping()
    public ResponseEntity<List<Course>> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return courseService.getCourseById(id);
    }

    @PostMapping()
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        return courseService.updateCourse(id, course);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> updateCourse(@PathVariable Long id) {
        return courseService.deleteCourse(id);
    }


    @GetMapping("/search")
    public ResponseEntity<Set<Course>> searchCourses(@RequestParam(required = false) String title, @RequestParam(required = false) String description, @RequestParam(required = false) String categoryName) {
        return courseService.searchCourses(categoryName, title, description);
    }

    @GetMapping("/search/rating/{order}")
    public ResponseEntity<Course> ratingCourses(@PathVariable String order) {
        return courseService.ratingCourses(order);
    }
}