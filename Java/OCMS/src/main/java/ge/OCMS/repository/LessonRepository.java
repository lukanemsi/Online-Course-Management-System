package ge.OCMS.repository;

import ge.OCMS.entity.Course;
import ge.OCMS.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("SELECT l FROM Lesson l " +
            "WHERE (:topic IS NULL OR LOWER(l.topic) LIKE LOWER(CONCAT('%', :topic, '%'))) " +
            "AND (:content IS NULL OR LOWER(l.content) LIKE LOWER(CONCAT('%', :content, '%')))")
    Set<Lesson> searchLesson(@Param("topic") String topic,
                             @Param("content") String content);

    List<Lesson> findByCourse(Course course);
}
