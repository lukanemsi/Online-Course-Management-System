package ge.OCMS.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ge.OCMS.configuration.jwt.JwtService;
import ge.OCMS.entity.Course;
import ge.OCMS.entity.Review;
import ge.OCMS.service.ReviewService;
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
@WebMvcTest(ReviewController.class)
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    private Review review;
    private Course course;


    @BeforeEach
    public void setUp() {
        course = new Course();
        course.setCourseId(1L);
        course.setTitle("Course Title");

        review = new Review();
        review.setId(1L);
        review.setStudentName("name");
        review.setComment("Great course!");
        review.setRating(5);
        review.setCourse(course);
    }

    @Test
    @WithMockUser
    public void getAllReviews_ReturnsListOfReviewsAndStatus200() throws Exception {
        List<Review> reviews = Collections.singletonList(review);
        Mockito.when(reviewService.getAllReviews()).thenReturn(ResponseEntity.ok(reviews));

        mockMvc.perform(get("/reviews")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(review.getId()))
                .andExpect(jsonPath("$[0].studentName").value(review.getStudentName()))
                .andExpect(jsonPath("$[0].comment").value(review.getComment()))
                .andExpect(jsonPath("$[0].rating").value(review.getRating()))
                .andExpect(jsonPath("$[0].course.title").value(course.getTitle()))
                .andExpect(jsonPath("$[0].course.lessons").doesNotExist())
                .andExpect(jsonPath("$[0].course.category").doesNotExist());

        verify(reviewService).getAllReviews();
    }

    @Test
    @WithMockUser
    public void getReviewById_ReturnsReviewAndStatus200() throws Exception {
        Mockito.when(reviewService.getReviewById(anyLong())).thenReturn(ResponseEntity.ok(review));

        mockMvc.perform(get("/reviews/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(review.getId()))
                .andExpect(jsonPath("$.studentName").value(review.getStudentName()))
                .andExpect(jsonPath("$.comment").value(review.getComment()))
                .andExpect(jsonPath("$.rating").value(review.getRating()))
                .andExpect(jsonPath("$.course.title").value(course.getTitle()))
                .andExpect(jsonPath("$.course.lessons").doesNotExist())
                .andExpect(jsonPath("$.course.category").doesNotExist());

        verify(reviewService).getReviewById(1L);
    }

    @Test
    @WithMockUser
    public void createReview_ReturnsCreatedReviewAndStatus201() throws Exception {
        Mockito.when(reviewService.createReview(any(Review.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(review));

        mockMvc.perform(post("/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(review.getId()))
                .andExpect(jsonPath("$.studentName").value(review.getStudentName()))
                .andExpect(jsonPath("$.comment").value(review.getComment()))
                .andExpect(jsonPath("$.rating").value(review.getRating()))
                .andExpect(jsonPath("$.course.title").value(course.getTitle()))
                .andExpect(jsonPath("$.course.lessons").doesNotExist())
                .andExpect(jsonPath("$.course.category").doesNotExist());

        verify(reviewService).createReview(any(Review.class));
    }

    @Test
    @WithMockUser
    public void updateReview_ReturnsStatus200() throws Exception {
        Mockito.when(reviewService.updateReview(anyLong(), any(Review.class))).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(put("/reviews/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(review))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(reviewService).updateReview(1L, review);
    }

    @Test
    @WithMockUser
    public void deleteReview_ReturnsStatus200() throws Exception {
        Mockito.when(reviewService.deleteReview(anyLong())).thenReturn(ResponseEntity.ok().build());

        mockMvc.perform(delete("/reviews/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(reviewService).deleteReview(1L);
    }

    @Test
    @WithMockUser
    public void findByCourse_ReturnsListOfReviewsAndStatus200() throws Exception {
        List<Review> reviews = Collections.singletonList(review);
        Mockito.when(reviewService.findByCourse(anyLong())).thenReturn(ResponseEntity.ok(reviews));

        mockMvc.perform(get("/reviews/search/course/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(review.getId()))
                .andExpect(jsonPath("$[0].studentName").value(review.getStudentName()))
                .andExpect(jsonPath("$[0].comment").value(review.getComment()))
                .andExpect(jsonPath("$[0].rating").value(review.getRating()))
                .andExpect(jsonPath("$[0].course.title").value(course.getTitle()))
                .andExpect(jsonPath("$[0].course.lessons").doesNotExist())
                .andExpect(jsonPath("$[0].course.category").doesNotExist());

        verify(reviewService).findByCourse(1L);
    }

    @Test
    @WithMockUser
    public void searchReviews_ReturnsSetOfReviewsAndStatus200() throws Exception {
        Set<Review> reviews = Set.of(review);
        Mockito.when(reviewService.searchReviews(any(), any(), any())).thenReturn(ResponseEntity.ok(reviews));

        mockMvc.perform(get("/reviews/search?studentName=name&comment=Great&rating=5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(review.getId()))
                .andExpect(jsonPath("$[0].studentName").value(review.getStudentName()))
                .andExpect(jsonPath("$[0].comment").value(review.getComment()))
                .andExpect(jsonPath("$[0].rating").value(review.getRating()))
                .andExpect(jsonPath("$[0].course.title").value(course.getTitle()))
                .andExpect(jsonPath("$[0].course.lessons").doesNotExist())
                .andExpect(jsonPath("$[0].course.category").doesNotExist());

        verify(reviewService).searchReviews("name", "Great", 5);
    }
}
