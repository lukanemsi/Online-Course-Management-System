package ge.OCMS.service;

import ge.OCMS.entity.CourseCategory;
import ge.OCMS.exception.custom.EntityNotFoundException;
import ge.OCMS.repository.CourseCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseCategoryServiceTest {

    @Mock
    private CourseCategoryRepository courseCategoryRepository;

    @InjectMocks
    private CourseCategoryService courseCategoryService;

    private CourseCategory category;

    @BeforeEach
    public void setUp() {
        category = new CourseCategory();
        category.setCategoryId(1L);
        category.setCategoryName("Test Category");
    }

    @Test
    public void getAllCategories_ReturnsAllCategories() {
        when(courseCategoryRepository.findAll()).thenReturn(Arrays.asList(category));

        ResponseEntity<List<CourseCategory>> response = courseCategoryService.getAllCategories();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(courseCategoryRepository).findAll();
    }

    @Test
    public void getCategoryById_ExistingId_ReturnsCategory() {
        when(courseCategoryRepository.findById(1L)).thenReturn(Optional.of(category));

        ResponseEntity<CourseCategory> response = courseCategoryService.getCategoryById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
        verify(courseCategoryRepository).findById(1L);
    }

    @Test
    public void getCategoryById_NonExistingId_ThrowsEntityNotFoundException() {
        when(courseCategoryRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            courseCategoryService.getCategoryById(1L);
        });
        assertEquals("Entity not found with given id: 1", exception.getMessage());
        verify(courseCategoryRepository).findById(1L);
    }

    @Test
    public void createCategory_Success_ReturnsCreatedCategory() {
        when(courseCategoryRepository.save(any(CourseCategory.class))).thenReturn(category);

        ResponseEntity<CourseCategory> response = courseCategoryService.createCategory(category);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(category, response.getBody());
        verify(courseCategoryRepository).save(category);
    }

    @Test
    public void updateCategory_ExistingId_UpdatesCategory() {
        when(courseCategoryRepository.existsById(1L)).thenReturn(true);
        when(courseCategoryRepository.save(any(CourseCategory.class))).thenReturn(category);

        ResponseEntity<Void> response = courseCategoryService.updateCategory(1L, category);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(courseCategoryRepository).existsById(1L);
        verify(courseCategoryRepository).save(category);
    }

    @Test
    public void updateCategory_NonExistingId_ThrowsEntityNotFoundException() {
        when(courseCategoryRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            courseCategoryService.updateCategory(1L, category);
        });
        assertEquals("Entity not found with given id: 1", exception.getMessage());
        verify(courseCategoryRepository).existsById(1L);
    }

    @Test
    public void deleteCategory_ExistingId_DeletesCategory() {
        when(courseCategoryRepository.existsById(1L)).thenReturn(true);

        ResponseEntity<Void> response = courseCategoryService.deleteCategory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(courseCategoryRepository).existsById(1L);
        verify(courseCategoryRepository).deleteById(1L);
    }

    @Test
    public void deleteCategory_NonExistingId_DoesNotThrow() {
        when(courseCategoryRepository.existsById(1L)).thenReturn(false);

        ResponseEntity<Void> response = courseCategoryService.deleteCategory(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(courseCategoryRepository).existsById(1L);
        verify(courseCategoryRepository, never()).deleteById(anyLong());
    }
}
