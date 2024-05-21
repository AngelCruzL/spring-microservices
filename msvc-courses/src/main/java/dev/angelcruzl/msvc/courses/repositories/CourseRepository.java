package dev.angelcruzl.msvc.courses.repositories;

import dev.angelcruzl.msvc.courses.models.entities.Course;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepository extends CrudRepository<Course, Long> {

    @Modifying
    @Query("DELETE FROM CourseUser cu WHERE cu.userId = :userId")
    void deleteCourseUserByUserId(Long userId);
    
}
