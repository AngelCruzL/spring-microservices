package dev.angelcruzl.msvc.users.integration;

import dev.angelcruzl.msvc.users.models.entities.User;
import dev.angelcruzl.msvc.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryITests extends AbstractionContainerBaseTest {

    @Autowired
    private UserRepository repository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
            .name("Ángel Cruz")
            .email("me@angelcruzl.dev")
            .password("123456")
            .build();
    }

    @BeforeEach
    public void tearDown() {
        repository.deleteAll();
    }

    @DisplayName("Integration test for [UserRepository] save user operation")
    @Test
    public void givenUserObject_whenSave_thenReturnSavedUser() {

        // given - precondition or setup

        // when - action or the behaviour that we are going test
        User savedUser = repository.save(user);

        // then - verify the output
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);

    }

    @DisplayName("Integration test for [UserRepository] get all users operation")
    @Test
    public void givenUsersList_whenFindAll_thenReturnUsersList() {

        // given - precondition or setup
        User user2 = User.builder()
            .name("John Doe")
            .email("john@doe")
            .password("123456")
            .build();

        repository.save(user);
        repository.save(user2);

        // when - action or the behaviour that we are going test
        List<User> usersList = (List<User>) repository.findAll();

        // then - verify the output
        assertThat(usersList).isNotNull();
        assertThat(usersList.size()).isEqualTo(2);

    }

    @DisplayName("Integration test for [UserRepository] get user by email operation")
    @Test
    public void givenUserObject_whenFindByEmail_thenReturnUserObject() {

        // given - precondition or setup
        repository.save(user);

        // when - action or the behaviour that we are going test
        User foundUser = repository.findByEmail(user.getEmail()).get();

        // then - verify the output
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());

    }

    @DisplayName("Integration test for [UserRepository] get user by id operation")
    @Test
    public void givenUserId_whenFindById_thenReturnUserObject() {

        // given - precondition or setup
        repository.save(user);

        // when - action or the behaviour that we are going test
        User foundUser = repository.findById(user.getId()).get();

        // then - verify the output
        assertThat(foundUser).isNotNull();

    }

    @DisplayName("Integration test for [UserRepository] update user operation")
    @Test
    public void givenUserObject_whenUpdate_thenReturnUpdatedUser() {

        // given - precondition or setup
        repository.save(user);
        user.setName("Updated Name");

        // when - action or the behaviour that we are going test
        User updatedUser = repository.save(user);

        // then - verify the output
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getName()).isEqualTo("Updated Name");

    }

    @DisplayName("Integration test for [UserRepository] delete user by id operation")
    @Test
    public void givenUserId_whenDeleteById_thenUserIsDeleted() {

        // given - precondition or setup
        repository.save(user);

        // when - action or the behaviour that we are going test
        repository.deleteById(user.getId());

        // then - verify the output
        assertThat(repository.findById(user.getId())).isEmpty();

    }

}
