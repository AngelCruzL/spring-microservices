package dev.angelcruzl.msvc.courses.services;

import dev.angelcruzl.msvc.courses.entities.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    List<Course> findAll();
    Optional<Course> findById(Long id);
    Course save(Course course);
    void deleteById(Long id);
}
