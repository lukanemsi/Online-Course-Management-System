package ge.OCMS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.OCMS.configuration.jwt.JwtService;
import ge.OCMS.entity.CourseCategory;
import ge.OCMS.service.CourseCategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CourseCategoryController.class)
public class CourseCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseCategoryService courseCategoryService;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private JwtService jwtService;

    private CourseCategory category;

    @BeforeEach
    public void setUp() {
        category = new CourseCategory();
        category.setCategoryId(1L);
        category.setCategoryName("Programming");
    }

    @Test
    @WithMockUser
    public void getAllCategories_ReturnsListOfCategoriesAndStatus200() throws Exception {
        List<CourseCategory> categories = Collections.singletonList(category);
        Mockito.when(courseCategoryService.getAllCategories()).thenReturn(ResponseEntity.ok(categories));

        mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].categoryId").value(category.getCategoryId()))
                .andExpect(jsonPath("$[0].categoryName").value(category.getCategoryName()));

        verify(courseCategoryService).getAllCategories();
    }

    @Test
    @WithMockUser
    public void getCategoryById_ReturnsCategoryAndStatus200() throws Exception {
        Mockito.when(courseCategoryService.getCategoryById(anyLong())).thenReturn(ResponseEntity.ok(category));

        mockMvc.perform(get("/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categoryId").value(category.getCategoryId()))
                .andExpect(jsonPath("$.categoryName").value(category.getCategoryName()));

        verify(courseCategoryService).getCategoryById(1L);
    }

    @Test
    @WithMockUser
    public void createCategory_ReturnsCreatedCategoryAndStatus201() throws Exception {
        Mockito.when(courseCategoryService.createCategory(any(CourseCategory.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(category));

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.categoryId").value(category.getCategoryId()))
                .andExpect(jsonPath("$.categoryName").value(category.getCategoryName()));

        verify(courseCategoryService).createCategory(any(CourseCategory.class));
    }


    @Test
    @WithMockUser
    public void updateCategory_ReturnsStatus200() throws Exception {
        Mockito.when(courseCategoryService.updateCategory(anyLong(), any(CourseCategory.class))).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(put("/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(courseCategoryService).updateCategory(1L, category);
    }


    @Test
    @WithMockUser
    public void deleteCategory_ReturnsStatus200() throws Exception {
        Mockito.when(courseCategoryService.deleteCategory(anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/categories/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());


        verify(courseCategoryService).deleteCategory(1L);
    }
}
