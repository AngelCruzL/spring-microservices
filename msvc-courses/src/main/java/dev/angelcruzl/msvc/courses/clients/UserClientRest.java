package dev.angelcruzl.msvc.courses.clients;

import dev.angelcruzl.msvc.courses.models.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "${msvc.users.name}")
public interface UserClientRest {

    @GetMapping("/{id}")
    User findById(@PathVariable Long id);

    @PostMapping("/")
    User create(@RequestBody User user);

    @GetMapping("/users-by-course")
    List<User> listByIds(@RequestParam Iterable<Long> ids,
                         @RequestHeader(value = "Authorization", required = true) String token);

}
