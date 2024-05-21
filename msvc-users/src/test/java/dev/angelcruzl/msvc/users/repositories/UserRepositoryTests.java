package dev.angelcruzl.msvc.users.repositories;

import dev.angelcruzl.msvc.users.models.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository repository;

    private User user;

    @BeforeEach
    public void setUp() {
        repository.deleteAll();

        user = User.builder()
            .name("Angel")
            .email("me@angelcruzl.dev")
            .password("password")
            .build();
    }

    @DisplayName("Junit test for [UserRepository] save user method")
    @Test
    public void givenUserObject_whenSave_thenReturnSavedUser() {

        User savedUser = repository.save(user);
        User foundUser = repository.findByEmail(user.getEmail()).get();

        assertThat(foundUser).isEqualTo(savedUser);
        assertThat(foundUser.getEmail()).isEqualTo(savedUser.getEmail());
        assertThat(foundUser.getId()).isGreaterThan(0);

    }

    @DisplayName("JUnit test for [UserRepository] find all users method")
    @Test
    public void givenUsersList_whenFindAll_thenReturnUsersList() {

        User user2 = User.builder()
            .name("John")
            .email("john@doe")
            .password("password")
            .build();

        repository.save(user);
        repository.save(user2);

        List<User> usersList = new ArrayList<>();
        repository.findAll().forEach(usersList::add);

        assertThat(usersList).isNotNull();
        assertThat(usersList.size()).isEqualTo(2);

    }

    @DisplayName("Junit test for [UserRepository] get user by id method")
    @Test
    public void givenUserId_whenFindById_thenReturnFoundUser() {

        repository.save(user);
        User foundUser = repository.findById(user.getId()).get();

        assertThat(foundUser).isNotNull();

    }

    @DisplayName("Junit test for [UserRepository] update user method")
    @Test
    public void givenUserObject_whenUpdate_thenReturnUpdatedUser() {

        repository.save(user);
        user.setName("Updated Name");
        repository.save(user);

        User foundUser = repository.findById(user.getId()).get();

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getName()).isEqualTo("Updated Name");

    }

    @DisplayName("Junit test for [UserRepository] delete user by id method")
    @Test
    public void givenUserId_whenDeleteById_thenUserIsDeleted() {

        repository.save(user);
        repository.deleteById(user.getId());

        assertThat(repository.findById(user.getId())).isEmpty();

    }

    @DisplayName("Junit test for [UserRepository] find by email method")
    @Test
    public void givenUserEmail_whenFindByEmail_thenReturnFoundUser() {

        repository.save(user);
        User foundUser = repository.findByEmail(user.getEmail()).get();

        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());

    }

}
