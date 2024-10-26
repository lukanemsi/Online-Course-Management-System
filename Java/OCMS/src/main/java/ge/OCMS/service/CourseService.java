package ge.OCMS.service;

import ge.OCMS.entity.Course;
import ge.OCMS.exception.custom.EntityNotFoundException;
import ge.OCMS.exception.custom.InvalidRequestException;
import ge.OCMS.repository.CourseRepository;
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
public class CourseService {

    private final CourseRepository courseRepository;

    private final EntityManager entityManager;

    public ResponseEntity<List<Course>> getAllCourses() {
        return ResponseEntity.ok(courseRepository.findAll());
    }

    public ResponseEntity<Course> getCourseById(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        return course.map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with given id: " + id));
    }

    @Transactional
    public ResponseEntity<Course> createCourse(Course course) {
        Course savedCourse = courseRepository.save(course);
        courseRepository.flush();
        entityManager.refresh(savedCourse);
        log.trace("Add Course: {}", JsonConverter.toJson(savedCourse));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }

    @Transactional
    public ResponseEntity<Void> updateCourse(Long id, Course course) {
        if (!courseRepository.existsById(id))
            throw new EntityNotFoundException("Entity not found with given id: " + id);
        course.setCourseId(id);
        courseRepository.save(course);
        log.trace("Update Course: {}", JsonConverter.toJson(course));
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<Void> deleteCourse(Long id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            log.trace("Delete Course with id: {}", id);
        }
        return ResponseEntity.ok().build();
    }
    public ResponseEntity<Course> ratingCourses(String order){
        Course course = null;
        List<Course> courses = courseRepository.findCourseWithRatings();

        if(courses.isEmpty())
            return ResponseEntity.ok(course);
        if("first".equalsIgnoreCase(order))
            course = courses.get(0);
        else if("last".equalsIgnoreCase(order))
            course = courses.get(courses.size() - 1);
        else
            throw new InvalidRequestException("Order Path variable can only be 'first' or 'last'");

        return ResponseEntity.ok(course);
    }

    public ResponseEntity<Set<Course>> searchCourses(String categoryName,String title,String description){
        return ResponseEntity.ok(courseRepository.searchCourses(categoryName, title, description));
    }

}