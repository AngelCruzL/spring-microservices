package dev.angelcruzl.msvc.users.repositories;

import dev.angelcruzl.msvc.users.models.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
