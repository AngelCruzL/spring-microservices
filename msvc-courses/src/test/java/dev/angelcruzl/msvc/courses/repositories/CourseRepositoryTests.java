package dev.angelcruzl.msvc.courses.repositories;

import dev.angelcruzl.msvc.courses.models.entities.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class CourseRepositoryTests {

    @Autowired
    private CourseRepository repository;

    private Course course;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();

        course = Course.builder()
            .name("Aprende microservicios con spring boot")
            .build();
    }

    @DisplayName("Junit test for [CourseRepository] save course method")
    @Test
    public void givenCourseObject_whenSave_thenReturnSavedCourse() {

        Course savedCourse = repository.save(course);

        assertThat(savedCourse).isNotNull();
        assertThat(savedCourse.getId()).isGreaterThan(0);

    }

}
