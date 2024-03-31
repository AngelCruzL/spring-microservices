package dev.angelcruzl.msvc.courses.services;

import dev.angelcruzl.msvc.courses.clients.UserClientRest;
import dev.angelcruzl.msvc.courses.models.User;
import dev.angelcruzl.msvc.courses.models.entities.Course;
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
    public Optional<User> createUser(Long courseId, User user) {
        return Optional.empty();
    }

    @Override
    public Optional<User> assignUser(Long courseId, User user) {
        return Optional.empty();
    }

    @Override
    public Optional<User> unassignUser(Long courseId, User user) {
        return Optional.empty();
    }
}
