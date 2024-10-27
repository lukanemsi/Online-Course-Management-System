package ge.OCMS.controller;

import ge.OCMS.entity.Enrollment;
import ge.OCMS.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @GetMapping()
    public ResponseEntity<List<Enrollment>> getAllEnrollments() {
        return enrollmentService.getAllEnrollments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Enrollment> getEnrollmentById(@PathVariable Long id) {
        return enrollmentService.getEnrollmentById(id);
    }

    @PostMapping()
    public ResponseEntity<Enrollment> createEnrollment(@RequestBody Enrollment enrollment) {
        return enrollmentService.createEnrollment(enrollment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEnrollment(@PathVariable Long id, @RequestBody Enrollment enrollment) {
        return enrollmentService.updateEnrollment(id, enrollment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long id) {
        return enrollmentService.deleteEnrollment(id);
    }

    @GetMapping("/search/user/{id}")
    public ResponseEntity<List<Enrollment>> getEnrollmentByUser(@PathVariable Long id) {
        return enrollmentService.getEnrollmentByUser(id);
    }

    @GetMapping("/search/course/{id}")
    public ResponseEntity<List<Enrollment>> getCourseByUser(@PathVariable Long id) {
        return enrollmentService.getEnrollmentByCourse(id);
    }

}
