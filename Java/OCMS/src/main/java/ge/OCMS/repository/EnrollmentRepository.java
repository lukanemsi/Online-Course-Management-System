package ge.OCMS.repository;

import ge.OCMS.entity.Course;
import ge.OCMS.entity.Enrollment;
import ge.OCMS.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findByUser(User user);

    List<Enrollment> findByCourse(Course course);

}