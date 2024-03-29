package dev.angelcruzl.msvc.courses.controllers;

import dev.angelcruzl.msvc.courses.entities.Course;
import dev.angelcruzl.msvc.courses.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class CourseController {
    @Autowired
    private CourseService service;

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Course> course = service.findById(id);
        if (course.isPresent()) {
            return ResponseEntity.ok(course.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Course course) {
        Course newCourse = service.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Course course) {
        Optional<Course> optionalCourse = service.findById(id);
        if (optionalCourse.isPresent()) {
            Course updatedCourse = optionalCourse.get();
            updatedCourse.setName(course.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(updatedCourse));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Optional<Course> course = service.findById(id);
        if (course.isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
