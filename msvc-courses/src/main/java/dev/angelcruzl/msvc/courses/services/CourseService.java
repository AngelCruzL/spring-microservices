package dev.angelcruzl.msvc.courses.services;

import dev.angelcruzl.msvc.courses.models.User;
import dev.angelcruzl.msvc.courses.models.entities.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> findAll();

    Optional<Course> findById(Long id);

    Optional<Course> findByIdWithUsers(Long id);

    Course save(Course course);

    void deleteById(Long id);

    Optional<User> createUser(Long courseId, User user);

    Optional<User> assignUser(Long courseId, User user);

    Optional<User> unassignUser(Long courseId, User user);
}
