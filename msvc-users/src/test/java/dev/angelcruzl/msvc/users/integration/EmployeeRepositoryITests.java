package dev.angelcruzl.msvc.users.integration;

import dev.angelcruzl.msvc.users.models.entities.User;
import dev.angelcruzl.msvc.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryITests extends AbstractionContainerBaseTest {

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

    @DisplayName("JUnit test for save user operation")
    @Test
    public void givenUserObject_whenSave_thenReturnSavedUser() {

        // given - precondition or setup

        // when - action or the behaviour that we are going test
        User savedUser = repository.save(user);

        // then - verify the output
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isGreaterThan(0);

    }

}
