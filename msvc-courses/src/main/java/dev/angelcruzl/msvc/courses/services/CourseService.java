package dev.angelcruzl.msvc.courses.services;

import dev.angelcruzl.msvc.courses.models.User;
import dev.angelcruzl.msvc.courses.models.entities.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> findAll();

    Optional<Course> findById(Long id);

    Optional<Course> findByIdWithUsers(Long id, String token);

    Course save(Course course);

    void deleteById(Long id);

    void deleteCourseUserByUserId(Long userId);

    Optional<User> createUser(Long courseId, User user, String taken);

    Optional<User> assignUser(Long courseId, User user, String taken);

    Optional<User> unassignUser(Long courseId, User user, String taken);
}
