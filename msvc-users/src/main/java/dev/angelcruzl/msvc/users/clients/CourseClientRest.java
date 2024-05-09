package dev.angelcruzl.msvc.users.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${msvc.courses.name}", url = "${msvc.courses.url}")
public interface CourseClientRest {

    @DeleteMapping("/delete-user/{userId}")
    void deleteCourseUserByUserId(@PathVariable Long userId);

}
