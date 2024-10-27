package ge.OCMS.repository;

import ge.OCMS.entity.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Set;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE " +
            "(:categoryName IS NULL OR LOWER(c.category.categoryName) LIKE LOWER(CONCAT('%', :categoryName, '%'))) AND " +
            "(:title IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:description IS NULL OR LOWER(c.description) LIKE LOWER(CONCAT('%', :description, '%')))")
    Set<Course> searchCourses(@Param("categoryName") String categoryName,
                              @Param("title") String title,
                              @Param("description") String description);
    @EntityGraph(attributePaths = {"lessons"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT c FROM Course c " +
            "JOIN Review r ON c.courseId = r.course.courseId " +
            "GROUP BY c " +
            "ORDER BY AVG(r.rating) DESC")
    List<Course> findCourseWithRatings();
}