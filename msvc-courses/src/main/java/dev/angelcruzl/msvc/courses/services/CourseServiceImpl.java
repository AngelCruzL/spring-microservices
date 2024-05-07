package dev.angelcruzl.msvc.courses.services;

import dev.angelcruzl.msvc.courses.clients.UserClientRest;
import dev.angelcruzl.msvc.courses.models.User;
import dev.angelcruzl.msvc.courses.models.entities.Course;
import dev.angelcruzl.msvc.courses.models.entities.CourseUser;
import dev.angelcruzl.msvc.courses.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository repository;

    @Autowired
    private UserClientRest client;

    @Override
    @Transactional(readOnly = true)
    public List<Course> findAll() {
        return (List<Course>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Course> findByIdWithUsers(Long id) {
        Optional<Course> optionalCourse = repository.findById(id);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            if (!course.getCourseUsers().isEmpty()) {
                List<Long> ids = course.getCourseUsers().stream()
                        .map(CourseUser::getUserId).toList();

                List<User> users = client.listByIds(ids);
                course.setUsers(users);
            }

            return Optional.of(course);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Course save(Course course) {
        return repository.save(course);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<User> createUser(Long courseId, User user) {
        Optional<Course> optionalCourse = repository.findById(courseId);
        if (optionalCourse.isPresent()) {
            User newUser = client.create(user);

            Course course = optionalCourse.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(newUser.getId());

            course.addCourseUser(courseUser);
            repository.save(course);

            return Optional.of(newUser);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> assignUser(Long courseId, User user) {
        Optional<Course> optionalCourse = repository.findById(courseId);
        if (optionalCourse.isPresent()) {
            User userMsvc = client.findById(user.getId());

            Course course = optionalCourse.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMsvc.getId());

            course.addCourseUser(courseUser);
            repository.save(course);

            return Optional.of(userMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<User> unassignUser(Long courseId, User user) {
        Optional<Course> optionalCourse = repository.findById(courseId);
        if (optionalCourse.isPresent()) {
            User userMsvc = client.findById(user.getId());

            Course course = optionalCourse.get();
            CourseUser courseUser = new CourseUser();
            courseUser.setUserId(userMsvc.getId());

            course.removeCourseUser(courseUser);
            repository.save(course);

            return Optional.of(userMsvc);
        }

        return Optional.empty();
    }
}
