package ge.OCMS.repository;

import ge.OCMS.entity.Course;
import ge.OCMS.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM Review r " +
            "WHERE (:studentName IS NULL OR LOWER(r.studentName) LIKE LOWER(CONCAT('%', :studentName, '%'))) " +
            "AND (:comment IS NULL OR LOWER(r.comment) LIKE LOWER(CONCAT('%', :comment, '%'))) " +
            "AND (:rating IS NULL OR r.rating = :rating)")
    Set<Review> searchReview(@Param("studentName") String studentName,
                             @Param("comment") String comment,
                             @Param("rating") Integer rating);

    List<Review> findByCourse(Course course);
}
