package ge.OCMS.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Table(name = "LESSON")
@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LESSON_ID")
    @EqualsAndHashCode.Include
    private Long lessonId;

    @Column(name = "TOPIC", nullable = false)
    private String topic;

    @Column(name = "CONTENT", length = 2000)
    private String content;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "COURSE_ID", nullable = false)
    private Course course;

}
