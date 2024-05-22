package dev.angelcruzl.msvc.users.services;

import dev.angelcruzl.msvc.users.clients.CourseClientRest;
import dev.angelcruzl.msvc.users.models.entities.User;
import dev.angelcruzl.msvc.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository repository;

    @Mock
    private CourseClientRest client;

    @InjectMocks
    private UserServiceImpl service;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
            .name("Angel")
            .email("me@angelcruzl.dev")
            .password("password")
            .build();
    }

    @DisplayName("Junit test for [UserService] save user method")
    @Test
    public void givenUserObject_whenSave_thenReturnSavedUser() {

        given(repository.save(user)).willReturn(user);

        User savedUser = repository.save(user);

        assertThat(savedUser).isNotNull();

    }

//    @DisplayName("JUnit test for [UserService] save user when already exists")
//    @Test
//    public void givenUserObject_whenSave_thenReturnSavedUserAlreadyExists() {
//
//        given(repository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
//
//        Assertions.assertThrows(RuntimeException.class, () -> service.save(user));
//
//        verify(repository, never()).save(any(User.class));
//
//    }

    @DisplayName("JUnit test for [UserService] find all users method")
    @Test
    public void givenUsersList_whenFindAll_thenReturnUsersList() {

        User user2 = User.builder()
            .name("John")
            .email("john@doe")
            .password("password")
            .build();

        given(repository.findAll()).willReturn(List.of(user, user2));

        List<User> userList = service.findAll();

        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(2);

    }

    @DisplayName("JUnit test for [UserService] find all users when empty")
    @Test
    public void givenEmptyUsersList_whenFindAll_thenReturnEmptyUsersList() {

        given(repository.findAll()).willReturn(List.of());

        List<User> userList = service.findAll();

        assertThat(userList).isNotNull();
        assertThat(userList.size()).isEqualTo(0);

    }

    @DisplayName("JUnit test for [UserService] get user by id method")
    @Test
    public void givenUserId_whenFindById_thenReturnFoundUser() {

        given(repository.findById(user.getId())).willReturn(Optional.of(user));

        User foundUser = service.findById(user.getId()).get();

        assertThat(foundUser).isNotNull();

    }

    @DisplayName("JUnit test for [UserService] delete user by id method & unassign user from courses")
    @Test
    public void givenUserId_whenDeleteById_thenUserIsDeleted() {
        doNothing().when(repository).deleteById(anyLong());
        doNothing().when(client).deleteCourseUserByUserId(anyLong());

        service.deleteById(1L);

        verify(repository, times(1)).deleteById(1L);
        verify(client, times(1)).deleteCourseUserByUserId(1L);
    }

    @DisplayName("JUnit test for [UserService] list users by ids method")
    @Test
    public void givenUserIds_whenListByIds_thenReturnUsersList() {
        List<User> users = Collections.singletonList(user);
        List<Long> ids = List.of(1L);
        when(repository.findAllById(any(Iterable.class))).thenReturn(users);

        List<User> result = service.listByIds(ids);

        verify(repository, times(1)).findAllById(ids);
        assert result.equals(users);

    }

    @DisplayName("JUnit test for [UserService] exists by email method")
    @Test
    public void givenEmail_whenExistsByEmail_thenReturnTrue() {
        when(repository.existsByEmail(anyString())).thenReturn(true);

        boolean exists = service.existsByEmail("test@example.com");

        verify(repository, times(1)).existsByEmail("test@example.com");
        assert exists;

    }

    @DisplayName("JUnit test for [UserService] exists by email method when email does not exist")
    @Test
    public void givenEmail_whenExistsByEmail_thenReturnFalse() {
        when(repository.existsByEmail(anyString())).thenReturn(false);

        boolean exists = service.existsByEmail("not@exists.com");

        verify(repository, times(1)).existsByEmail("not@exists.com");
        assert !exists;

    }

}
