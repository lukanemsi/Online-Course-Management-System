package ge.OCMS.controller;


import ge.OCMS.entity.Review;
import ge.OCMS.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping()
    public ResponseEntity<List<Review>> getAllReviews() {
        return reviewService.getAllReviews();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id) {
        return reviewService.getReviewById(id);
    }

    @PostMapping()
    public ResponseEntity<Review> createReview(@RequestBody Review review) {
        return reviewService.createReview(review);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReview(@PathVariable Long id, @RequestBody Review review) {
        return reviewService.updateReview(id, review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        return reviewService.deleteReview(id);
    }

    @GetMapping("/search/course/{id}")
    public ResponseEntity<List<Review>> findByCourse(@PathVariable Long id) {
        return reviewService.findByCourse(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Set<Review>> searchReviews(@RequestParam(required = false) String studentName, @RequestParam(required = false) String comment, @RequestParam(required = false) Integer rating) {
        return reviewService.searchReviews(studentName, comment, rating);
    }
}