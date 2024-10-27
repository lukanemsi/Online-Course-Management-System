package ge.OCMS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.OCMS.configuration.jwt.JwtService;
import ge.OCMS.entity.Enrollment;
import ge.OCMS.service.EnrollmentService;
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
@WebMvcTest(EnrollmentController.class)
public class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentService enrollmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    private Enrollment enrollment;

    @BeforeEach
    public void setUp() {
        enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setActive(true);
    }

    @Test
    @WithMockUser
    public void getAllEnrollments_ReturnsListOfEnrollmentsAndStatus200() throws Exception {
        List<Enrollment> enrollments = Collections.singletonList(enrollment);
        Mockito.when(enrollmentService.getAllEnrollments()).thenReturn(ResponseEntity.ok(enrollments));

        mockMvc.perform(get("/enrollments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(enrollment.getId()))
                .andExpect(jsonPath("$[0].active").value(enrollment.getActive()))
                // Check that user and course are not included in the response
                .andExpect(jsonPath("$[0].user").doesNotExist())
                .andExpect(jsonPath("$[0].course").doesNotExist());

        verify(enrollmentService).getAllEnrollments();
    }

    @Test
    @WithMockUser
    public void getEnrollmentById_ReturnsEnrollmentAndStatus200() throws Exception {
        Mockito.when(enrollmentService.getEnrollmentById(anyLong())).thenReturn(ResponseEntity.ok(enrollment));

        mockMvc.perform(get("/enrollments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(enrollment.getId()))
                .andExpect(jsonPath("$.active").value(enrollment.getActive()))
                // Check that user and course are not included in the response
                .andExpect(jsonPath("$.user").doesNotExist())
                .andExpect(jsonPath("$.course").doesNotExist());

        verify(enrollmentService).getEnrollmentById(1L);
    }

    @Test
    @WithMockUser
    public void createEnrollment_ReturnsCreatedEnrollmentAndStatus201() throws Exception {
        Mockito.when(enrollmentService.createEnrollment(any(Enrollment.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(enrollment));

        mockMvc.perform(post("/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollment))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(enrollment.getId()))
                .andExpect(jsonPath("$.active").value(enrollment.getActive()))
                .andExpect(jsonPath("$.user").doesNotExist())
                .andExpect(jsonPath("$.course").doesNotExist());

        verify(enrollmentService).createEnrollment(any(Enrollment.class));
    }

    @Test
    @WithMockUser
    public void updateEnrollment_ReturnsStatus200() throws Exception {
        Mockito.when(enrollmentService.updateEnrollment(anyLong(), any(Enrollment.class))).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(put("/enrollments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollment))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(enrollmentService).updateEnrollment(1L, enrollment);
    }

    @Test
    @WithMockUser
    public void deleteEnrollment_ReturnsStatus200() throws Exception {
        Mockito.when(enrollmentService.deleteEnrollment(anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/enrollments/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(enrollmentService).deleteEnrollment(1L);
    }

    @Test
    @WithMockUser
    public void getEnrollmentByUser_ReturnsListOfEnrollmentsAndStatus200() throws Exception {
        List<Enrollment> enrollments = Collections.singletonList(enrollment);
        Mockito.when(enrollmentService.getEnrollmentByUser(anyLong())).thenReturn(ResponseEntity.ok(enrollments));

        mockMvc.perform(get("/enrollments/search/user/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(enrollment.getId()))
                .andExpect(jsonPath("$[0].active").value(enrollment.getActive()))
                .andExpect(jsonPath("$[0].user").doesNotExist())
                .andExpect(jsonPath("$[0].course").doesNotExist());

        verify(enrollmentService).getEnrollmentByUser(1L);
    }

    @Test
    @WithMockUser
    public void getEnrollmentByCourse_ReturnsListOfEnrollmentsAndStatus200() throws Exception {
        List<Enrollment> enrollments = Collections.singletonList(enrollment);
        Mockito.when(enrollmentService.getEnrollmentByCourse(anyLong())).thenReturn(ResponseEntity.ok(enrollments));

        mockMvc.perform(get("/enrollments/search/course/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(enrollment.getId()))
                .andExpect(jsonPath("$[0].active").value(enrollment.getActive()))
                .andExpect(jsonPath("$[0].user").doesNotExist())
                .andExpect(jsonPath("$[0].course").doesNotExist());

        verify(enrollmentService).getEnrollmentByCourse(1L);
    }
}
