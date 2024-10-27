package ge.OCMS.service;

import ge.OCMS.entity.CourseCategory;
import ge.OCMS.exception.custom.EntityNotFoundException;
import ge.OCMS.repository.CourseCategoryRepository;
import ge.OCMS.util.JsonConverter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class CourseCategoryService {

    private final CourseCategoryRepository courseCategoryRepository;

    public ResponseEntity<List<CourseCategory>> getAllCategories() {
        return ResponseEntity.ok(courseCategoryRepository.findAll());
    }

    public ResponseEntity<CourseCategory> getCategoryById(Long id) {
        Optional<CourseCategory> category = courseCategoryRepository.findById(id);
        return category.map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with given id: " + id));
    }

    @Transactional
    public ResponseEntity<CourseCategory> createCategory(CourseCategory category) {
        CourseCategory savedCategory = courseCategoryRepository.save(category);
        log.trace("Add CourseCategory: {}", JsonConverter.toJson(savedCategory));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @Transactional
    public ResponseEntity<Void> updateCategory(Long id, CourseCategory category) {
        if (!courseCategoryRepository.existsById(id))
            throw new EntityNotFoundException("Entity not found with given id: " + id);
        category.setCategoryId(id);
        courseCategoryRepository.save(category);
        log.trace("Update CourseCategory: {}", JsonConverter.toJson(category));
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<Void> deleteCategory(Long id) {
        if (courseCategoryRepository.existsById(id)) {
            courseCategoryRepository.deleteById(id);
            log.trace("Delete CourseCategory with id: {}", id);
        }
        return ResponseEntity.ok().build();
    }
}
