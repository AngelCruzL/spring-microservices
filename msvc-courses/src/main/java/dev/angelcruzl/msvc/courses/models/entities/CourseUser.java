package dev.angelcruzl.msvc.courses.models.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "courses_users", schema = "courses")
public class CourseUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof CourseUser courseUser)) {
            return false;
        }

        return userId != null && userId.equals(courseUser.getUserId());
    }
}
