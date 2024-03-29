package dev.angelcruzl.msvc.courses.repositories;

import dev.angelcruzl.msvc.courses.models.entities.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {
}
