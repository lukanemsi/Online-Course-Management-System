package ge.OCMS.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table(name = "COURSE_CATEGORY")
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CourseCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    @EqualsAndHashCode.Include
    private Long categoryId;

    @Column(name = "CATEGORY_NAME")
    private String categoryName;

}