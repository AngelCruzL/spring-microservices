package dev.angelcruzl.msvc.users.controllers;

import dev.angelcruzl.msvc.users.models.entities.User;
import dev.angelcruzl.msvc.users.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Environment env;

    private static ResponseEntity<Map<String, String>> validateUser(BindingResult result) {
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach(
            err -> errors.put(err.getField(), "El campo " + err.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @GetMapping("/crash")
    public void crash() {
        ((ConfigurableApplicationContext) context).close();
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        Map<String, Object> body = new HashMap<>();
        body.put("users", service.findAll());
        body.put("pod_info", env.getProperty("MY_POD_NAME") + ": " + env.getProperty("MY_POD_IP"));
        body.put("text", env.getProperty("config.text"));

        return ResponseEntity.ok(body);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            return validateUser(result);
        }
        if (service.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(
                Map.of("message", "El correo electrónico ya ha sido registrado"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<User> optionalUser = service.findById(id);
        if (optionalUser.isPresent()) {
            return ResponseEntity.ok(optionalUser.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody User user, @PathVariable Long id,
                                    BindingResult result) {
        if (result.hasErrors()) {
            return validateUser(result);
        }
        Optional<User> optionalUser = service.findById(id);
        if (optionalUser.isPresent()) {
            User userDb = optionalUser.get();
            if (user.getEmail().equalsIgnoreCase(userDb.getEmail()) &&
                service.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body(
                    Map.of("message", "El correo electrónico ya ha sido registrado"));
            }
            userDb.setName(user.getName());
            userDb.setEmail(user.getEmail());
            userDb.setPassword(user.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.save(userDb));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<User> optionalUser = service.findById(id);
        if (optionalUser.isPresent()) {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/users-by-course")
    public ResponseEntity<?> listByIds(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(service.listByIds(ids));
    }

    @GetMapping("/authorized")
    public Map<String, Object> authorized(@RequestParam(name = "code") String code) {
        return Collections.singletonMap("code", code);
    }

}
