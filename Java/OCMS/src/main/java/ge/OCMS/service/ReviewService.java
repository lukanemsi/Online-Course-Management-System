package ge.OCMS.service;

import ge.OCMS.entity.Course;
import ge.OCMS.entity.Review;
import ge.OCMS.exception.custom.EntityNotFoundException;
import ge.OCMS.repository.CourseRepository;
import ge.OCMS.repository.ReviewRepository;
import ge.OCMS.util.AuthenticatedUserUtil;
import ge.OCMS.util.JsonConverter;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final CourseRepository courseRepository;

    private final EntityManager entityManager;

    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(reviewRepository.findAll());
    }

    public ResponseEntity<Review> getReviewById(Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        return review.map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("Entity not found with given id: " + id));
    }

    @Transactional
    public ResponseEntity<Review> createReview(Review review) {
        Optional<UserDetails> user = AuthenticatedUserUtil.getAuthenticatedUser();
        user.ifPresent(userDetails -> review.setStudentName(userDetails.getUsername()));
        Review savedReview = reviewRepository.save(review);
        reviewRepository.flush();
        entityManager.refresh(savedReview);
        log.trace("Add Review: {}", JsonConverter.toJson(savedReview));
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReview);
    }

    @Transactional
    public ResponseEntity<Void> updateReview(Long id, Review review) {
        if (!reviewRepository.existsById(id))
            throw new EntityNotFoundException("Entity not found with given id: " + id);
        review.setId(id);
        Optional<UserDetails> user = AuthenticatedUserUtil.getAuthenticatedUser();
        user.ifPresent(userDetails -> review.setStudentName(userDetails.getUsername()));
        reviewRepository.save(review);
        log.trace("Update Review: {}", JsonConverter.toJson(review));
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<Void> deleteReview(Long id) {
        if (reviewRepository.existsById(id)) {
            reviewRepository.deleteById(id);
            log.trace("Delete Review with id: {}", id);
        }
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<List<Review>> findByCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Course not found with given id: " + id)
        );
        return ResponseEntity.ok(reviewRepository.findByCourse(course));
    }

    public ResponseEntity<Set<Review>> searchReviews(String studentName, String comment,Integer rating){
        return ResponseEntity.ok(reviewRepository.searchReview(studentName,comment,rating));
    }
}
