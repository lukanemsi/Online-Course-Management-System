package ge.OCMS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.OCMS.configuration.jwt.JwtService;
import ge.OCMS.entity.Course;
import ge.OCMS.service.CourseService;
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
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    private Course course;

    @BeforeEach
    public void setUp() {
        course = new Course();
        course.setCourseId(1L);
        course.setTitle("Java Programming");
        course.setDescription("Learn Java from scratch.");
    }

    @Test
    @WithMockUser
    public void getAllCourses_ReturnsListOfCoursesAndStatus200() throws Exception {
        List<Course> courses = Collections.singletonList(course);
        Mockito.when(courseService.getAllCourses()).thenReturn(ResponseEntity.ok(courses));

        mockMvc.perform(get("/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].courseId").value(course.getCourseId()))
                .andExpect(jsonPath("$[0].title").value(course.getTitle()))
                .andExpect(jsonPath("$[0].description").value(course.getDescription()))
                .andExpect(jsonPath("$[0].lessons").exists());

        verify(courseService).getAllCourses();
    }

    @Test
    @WithMockUser
    public void getCourseById_ReturnsCourseAndStatus200() throws Exception {
        Mockito.when(courseService.getCourseById(anyLong())).thenReturn(ResponseEntity.ok(course));

        mockMvc.perform(get("/courses/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.courseId").value(course.getCourseId()))
                .andExpect(jsonPath("$.title").value(course.getTitle()))
                .andExpect(jsonPath("$.description").value(course.getDescription()))
                .andExpect(jsonPath("$.lessons").exists());

        verify(courseService).getCourseById(1L);
    }

    @Test
    @WithMockUser
    public void createCourse_ReturnsCreatedCourseAndStatus201() throws Exception {
        Mockito.when(courseService.createCourse(any(Course.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(course));

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.courseId").value(course.getCourseId()))
                .andExpect(jsonPath("$.title").value(course.getTitle()))
                .andExpect(jsonPath("$.description").value(course.getDescription()));

        verify(courseService).createCourse(any(Course.class));
    }

    @Test
    @WithMockUser
    public void updateCourse_ReturnsStatus200() throws Exception {
        Mockito.when(courseService.updateCourse(anyLong(), any(Course.class))).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(put("/courses/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(courseService).updateCourse(1L, course);
    }

    @Test
    @WithMockUser
    public void deleteCourse_ReturnsStatus200() throws Exception {
        Mockito.when(courseService.deleteCourse(anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/courses/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                )
                .andExpect(status().isOk());

        verify(courseService).deleteCourse(1L);
    }

    @Test
    @WithMockUser
    public void searchCourses_ReturnsSetOfCoursesAndStatus200() throws Exception {
        Set<Course> courses = Set.of(course);
        Mockito.when(courseService.searchCourses(any(), any(), any())).thenReturn(ResponseEntity.ok(courses));

        mockMvc.perform(get("/courses/search")
                        .param("title", "Java Programming")
                        .param("description", "Learn Java")
                        .param("categoryName", "Programming")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].courseId").value(course.getCourseId()))
                .andExpect(jsonPath("$[0].title").value(course.getTitle()))
                .andExpect(jsonPath("$[0].description").value(course.getDescription()));

        verify(courseService).searchCourses("Programming", "Java Programming", "Learn Java");
    }

    @Test
    @WithMockUser
    public void ratingCourses_ReturnsCourseAndStatus200() throws Exception {
        Mockito.when(courseService.ratingCourses(any())).thenReturn(ResponseEntity.ok(course));

        mockMvc.perform(get("/courses/search/rating/{order}", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.courseId").value(course.getCourseId()))
                .andExpect(jsonPath("$.title").value(course.getTitle()))
                .andExpect(jsonPath("$.description").value(course.getDescription()));

        verify(courseService).ratingCourses("asc");
    }
}
