package ge.OCMS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.OCMS.configuration.jwt.JwtService;
import ge.OCMS.entity.Lesson;
import ge.OCMS.service.LessonService;
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
@WebMvcTest(LessonController.class)
public class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LessonService lessonService;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private JwtService jwtService;

    private Lesson lesson;

    @BeforeEach
    public void setUp() {
        lesson = new Lesson();
        lesson.setLessonId(1L);
        lesson.setTopic("Sample Topic");
        lesson.setContent("Sample content for the lesson.");
        // Set additional properties if needed
    }

    @Test
    @WithMockUser
    public void getAllLessons_ReturnsListOfLessonsAndStatus200() throws Exception {
        List<Lesson> lessons = Collections.singletonList(lesson);
        Mockito.when(lessonService.getAllLessons()).thenReturn(ResponseEntity.ok(lessons));

        mockMvc.perform(get("/lessons")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].lessonId").value(lesson.getLessonId()))
                .andExpect(jsonPath("$[0].topic").value(lesson.getTopic()))
                .andExpect(jsonPath("$[0].content").value(lesson.getContent()))
                // Check that course is not included in the response
                .andExpect(jsonPath("$[0].course").doesNotExist());

        verify(lessonService).getAllLessons();
    }

    @Test
    @WithMockUser
    public void getLessonById_ReturnsLessonAndStatus200() throws Exception {
        Mockito.when(lessonService.getLessonById(anyLong())).thenReturn(ResponseEntity.ok(lesson));

        mockMvc.perform(get("/lessons/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lessonId").value(lesson.getLessonId()))
                .andExpect(jsonPath("$.topic").value(lesson.getTopic()))
                .andExpect(jsonPath("$.content").value(lesson.getContent()))
                // Check that course is not included in the response
                .andExpect(jsonPath("$.course").doesNotExist());

        verify(lessonService).getLessonById(1L);
    }

    @Test
    @WithMockUser
    public void createLesson_ReturnsCreatedLessonAndStatus201() throws Exception {
        Mockito.when(lessonService.createLesson(any(Lesson.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(lesson));

        mockMvc.perform(post("/lessons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lesson))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.lessonId").value(lesson.getLessonId()))
                .andExpect(jsonPath("$.topic").value(lesson.getTopic()))
                .andExpect(jsonPath("$.content").value(lesson.getContent()))
                // Check that course is not included in the response
                .andExpect(jsonPath("$.course").doesNotExist());

        verify(lessonService).createLesson(any(Lesson.class));
    }

    @Test
    @WithMockUser
    public void updateLesson_ReturnsStatus200() throws Exception {
        Mockito.when(lessonService.updateLesson(anyLong(), any(Lesson.class))).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(put("/lessons/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lesson))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(lessonService).updateLesson(1L, lesson);
    }

    @Test
    @WithMockUser
    public void deleteLesson_ReturnsStatus200() throws Exception {
        Mockito.when(lessonService.deleteLesson(anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/lessons/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(lessonService).deleteLesson(1L);
    }

    @Test
    @WithMockUser
    public void findByCourse_ReturnsListOfLessonsAndStatus200() throws Exception {
        List<Lesson> lessons = Collections.singletonList(lesson);
        Mockito.when(lessonService.findByCourse(anyLong())).thenReturn(ResponseEntity.ok(lessons));

        mockMvc.perform(get("/lessons/search/course/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].lessonId").value(lesson.getLessonId()))
                .andExpect(jsonPath("$[0].topic").value(lesson.getTopic()))
                .andExpect(jsonPath("$[0].content").value(lesson.getContent()))
                .andExpect(jsonPath("$[0].course").doesNotExist());

        verify(lessonService).findByCourse(1L);
    }

    @Test
    @WithMockUser
    public void searchCourses_ReturnsSetOfLessonsAndStatus200() throws Exception {
        List<Lesson> lessons = Collections.singletonList(lesson);
        Mockito.when(lessonService.searchLessons(any(), any())).thenReturn(ResponseEntity.ok(Set.of(lesson)));

        mockMvc.perform(get("/lessons/search?topic=Sample&content=Sample")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].lessonId").value(lesson.getLessonId()))
                .andExpect(jsonPath("$[0].topic").value(lesson.getTopic()))
                .andExpect(jsonPath("$[0].content").value(lesson.getContent()))
                .andExpect(jsonPath("$[0].course").doesNotExist());

        verify(lessonService).searchLessons("Sample", "Sample");
    }
}
