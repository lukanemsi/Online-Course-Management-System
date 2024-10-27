package ge.OCMS.controller;

import ge.OCMS.entity.CourseCategory;
import ge.OCMS.service.CourseCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CourseCategoryController {

    private final CourseCategoryService courseCategoryService;

    @GetMapping()
    public ResponseEntity<List<CourseCategory>> getAllCategories() {
        return courseCategoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseCategory> getCategoryById(@PathVariable Long id) {
        return courseCategoryService.getCategoryById(id);
    }

    @PostMapping()
    public ResponseEntity<CourseCategory> createCategory(@RequestBody CourseCategory category) {
        return courseCategoryService.createCategory(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(@PathVariable Long id, @RequestBody CourseCategory category) {
        return courseCategoryService.updateCategory(id, category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        return courseCategoryService.deleteCategory(id);
    }
}
