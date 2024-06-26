package dev.angelcruzl.msvc.courses.controllers;

import dev.angelcruzl.msvc.courses.models.User;
import dev.angelcruzl.msvc.courses.models.entities.Course;
import dev.angelcruzl.msvc.courses.services.CourseService;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class CourseController {

    @Autowired
    private CourseService service;

    private static ResponseEntity<Map<String, String>> validateCourse(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(
            err -> errors.put(err.getField(), "El campo " + err.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id,
                                      @RequestHeader(value = "Authorization", required = true) String token) {
        Optional<Course> course = service.findByIdWithUsers(id, token);
        if (course.isPresent()) {
            return ResponseEntity.ok(course.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody Course course, BindingResult result) {
        if (result.hasErrors()) {
            return validateCourse(result);
        }
        Course newCourse = service.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @PathVariable Long id, @RequestBody Course course,
                                    BindingResult result) {
        if (result.hasErrors()) {
            return validateCourse(result);
        }
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

    @PostMapping("/create-user/{courseId}")
    public ResponseEntity<?> createUser(@PathVariable Long courseId, @RequestBody User user,
                                        @RequestHeader(value = "Authorization", required = true) String token) {
        Optional<User> optionalUser = null;
        try {
            optionalUser = service.createUser(courseId, user, token);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "No se pudo crear el usuario,"
                    + " error: " + e.getMessage()));
        }
        if (optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalUser.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/assign-user/{courseId}")
    public ResponseEntity<?> assignUser(@PathVariable Long courseId, @RequestBody User user,
                                        @RequestHeader(value = "Authorization", required = true) String token) {
        Optional<User> optionalUser = null;
        try {
            optionalUser = service.assignUser(courseId, user, token);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "No existe el usuario con el id "
                    + user.getId() + ", error: " + e.getMessage()));
        }
        if (optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(optionalUser.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/unassign-user/{courseId}")
    public ResponseEntity<?> unassignUser(@PathVariable Long courseId, @RequestBody User user,
                                          @RequestHeader(value = "Authorization", required = true) String token) {
        Optional<User> optionalUser = null;
        try {
            optionalUser = service.unassignUser(courseId, user, token);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Collections.singletonMap("message", "No existe el usuario con el id "
                    + user.getId() + ", error: " + e.getMessage()));
        }
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete-user/{userId}")
    public ResponseEntity<?> deleteCourseUserByUserId(@PathVariable Long userId) {
        service.deleteCourseUserByUserId(userId);
        return ResponseEntity.noContent().build();
    }

}
