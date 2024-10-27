package ge.OCMS.service;

import ge.OCMS.entity.Course;
import ge.OCMS.entity.Review;
import ge.OCMS.exception.custom.EntityNotFoundException;
import ge.OCMS.repository.CourseRepository;
import ge.OCMS.repository.ReviewRepository;
import ge.OCMS.util.AuthenticatedUserUtil;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ReviewService reviewService;

    private Review sampleReview;
    private Course sampleCourse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleCourse = new Course();
        sampleCourse.setCourseId(1L);
        sampleCourse.setTitle("Sample Course");

        sampleReview = new Review();
        sampleReview.setId(1L);
        sampleReview.setStudentName("testUser");
        sampleReview.setComment("Great course!");
        sampleReview.setRating(5);
        sampleReview.setCourse(sampleCourse);
    }

    @Test
    void whenGetAllReviews_thenReturnAllReviews() {
        when(reviewRepository.findAll()).thenReturn(List.of(sampleReview));

        ResponseEntity<List<Review>> response = reviewService.getAllReviews();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(sampleReview);
    }

    @Test
    void whenGetReviewById_withValidId_thenReturnReview() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.of(sampleReview));

        ResponseEntity<Review> response = reviewService.getReviewById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(sampleReview);
    }

    @Test
    void whenGetReviewById_withInvalidId_thenThrowException() {
        when(reviewRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.getReviewById(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Entity not found with given id: 1");
    }

    @Test
    void whenCreateReview_thenReturnCreatedReview() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        try (MockedStatic<AuthenticatedUserUtil> mockedAuthenticatedUserUtil = mockStatic(AuthenticatedUserUtil.class)) {
            mockedAuthenticatedUserUtil.when(AuthenticatedUserUtil::getAuthenticatedUser).thenReturn(Optional.of(userDetails));

            when(reviewRepository.save(sampleReview)).thenReturn(sampleReview);

            ResponseEntity<Review> response = reviewService.createReview(sampleReview);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(response.getBody()).isEqualTo(sampleReview);
            verify(reviewRepository).flush();
            verify(entityManager).refresh(sampleReview);
        }
    }

    @Test
    void whenUpdateReview_withValidId_thenUpdateReview() {
        when(reviewRepository.existsById(1L)).thenReturn(true);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testUser");

        try (MockedStatic<AuthenticatedUserUtil> mockedAuthenticatedUserUtil = mockStatic(AuthenticatedUserUtil.class)) {
            mockedAuthenticatedUserUtil.when(AuthenticatedUserUtil::getAuthenticatedUser).thenReturn(Optional.of(userDetails));

            ResponseEntity<Void> response = reviewService.updateReview(1L, sampleReview);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(reviewRepository).save(sampleReview);
        }
    }


    @Test
    void whenUpdateReview_withInvalidId_thenThrowException() {
        when(reviewRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> reviewService.updateReview(1L, sampleReview))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Entity not found with given id: 1");
    }

    @Test
    void whenDeleteReview_withValidId_thenDeleteReview() {
        when(reviewRepository.existsById(1L)).thenReturn(true);

        ResponseEntity<Void> response = reviewService.deleteReview(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(reviewRepository).deleteById(1L);
    }

    @Test
    void whenFindByCourse_withValidCourseId_thenReturnReviews() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(sampleCourse));
        when(reviewRepository.findByCourse(sampleCourse)).thenReturn(List.of(sampleReview));

        ResponseEntity<List<Review>> response = reviewService.findByCourse(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(sampleReview);
    }

    @Test
    void whenFindByCourse_withInvalidCourseId_thenThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> reviewService.findByCourse(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Course not found with given id: 1");
    }

    @Test
    void whenSearchReviews_thenReturnMatchingReviews() {
        when(reviewRepository.searchReview("testUser", "Great course!", 5)).thenReturn(Set.of(sampleReview));

        ResponseEntity<Set<Review>> response = reviewService.searchReviews("testUser", "Great course!", 5);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(sampleReview);
    }
}
