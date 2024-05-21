package dev.angelcruzl.msvc.courses.models.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses", schema = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String name;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "course_id")
    @Builder.Default
    private List<CourseUser> courseUsers = new ArrayList<>();

    @Transient
    @Builder.Default
    private List<dev.angelcruzl.msvc.courses.models.User> users = new ArrayList<>();

    public void addCourseUser(CourseUser courseUser) {
        if (this.courseUsers == null) this.courseUsers = new ArrayList<>();

        this.courseUsers.add(courseUser);
    }

    public void removeCourseUser(CourseUser courseUser) {
        if (this.courseUsers == null) return;

        this.courseUsers.remove(courseUser);
    }

}
