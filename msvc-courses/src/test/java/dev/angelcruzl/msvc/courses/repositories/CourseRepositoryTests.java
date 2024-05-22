package dev.angelcruzl.msvc.courses.repositories;

import dev.angelcruzl.msvc.courses.models.User;
import dev.angelcruzl.msvc.courses.models.entities.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

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

    @DisplayName("JUnit test for [CourseRepository] find all courses method")
    @Test
    public void givenCoursesList_whenFindAll_thenReturnCoursesList() {

        Course course2 = Course.builder()
            .name("Angular avanzado")
            .build();

        repository.save(course);
        repository.save(course2);

        List<Course> courseList = new ArrayList<>();
        repository.findAll().forEach(courseList::add);

        assertThat(courseList).isNotNull();
        assertThat(courseList.size()).isEqualTo(2);

    }

    @DisplayName("JUnit test for [CourseRepository] get user by course id")
    @Test
    public void givenCourseId_whenFindById_thenReturnCourse() {

        Course savedCourse = repository.save(course);
        Course foundCourse = repository.findById(savedCourse.getId()).get();

        assertThat(foundCourse).isNotNull();

    }

    @DisplayName("JUnit test for [CourseRepository] update course method")
    @Test
    public void givenCourseObject_whenUpdate_thenReturnUpdatedCourse() {

        Course savedCourse = repository.save(course);
        savedCourse.setName("Curso avanzado de microservicios con spring boot");

        Course updatedCourse = repository.save(savedCourse);

        assertThat(updatedCourse).isNotNull();
        assertThat(updatedCourse.getName()).isEqualTo(savedCourse.getName());

    }

    @DisplayName("JUnit test for [CourseRepository] delete course method")
    @Test
    public void givenCourseId_whenDeleteById_thenCourseIsDeleted() {

        Course savedCourse = repository.save(course);
        repository.deleteById(savedCourse.getId());

        assertThat(repository.findById(savedCourse.getId())).isEmpty();

    }

    @DisplayName("JUnit test for [CourseRepository] delete course user by user id")
    @Test
    public void givenUserId_whenDeleteCourseUserByUserId_thenCourseUserIsDeleted() {

        Long userId = 1L;

        User user = User.builder()
            .id(userId)
            .name("John")
            .email("john@doe")
            .password("password")
            .build();

        course = Course.builder()
            .name("Aprende microservicios con spring boot")
            .users(List.of(user))
            .build();

        Course savedCourse = repository.save(course);
        repository.deleteCourseUserByUserId(userId);

        Course courseDb = repository.findById(savedCourse.getId()).get();
        assertThat(courseDb.getCourseUsers().size()).isEqualTo(0);

    }

}
