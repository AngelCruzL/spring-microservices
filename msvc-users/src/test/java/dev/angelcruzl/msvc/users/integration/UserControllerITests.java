package dev.angelcruzl.msvc.users.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.angelcruzl.msvc.users.models.entities.User;
import dev.angelcruzl.msvc.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerITests extends AbstractionContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

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

    @DisplayName("[Integration] POST / - Success")
    @Test
    public void givenUserObject_whenPostUser_thenStatus201() throws Exception {

        // given - precondition or setup

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)));

        // then - verify the result or output using assert statements
        response.andExpect(status().isCreated())
            .andDo(print())
            .andExpect(jsonPath("$.name", is(user.getName())))
            .andExpect(jsonPath("$.email", is(user.getEmail())))
            .andExpect(jsonPath("$.password", not(user.getPassword())));

    }

    @DisplayName("[Integration] POST / - Failure")
    @Test
    public void givenUserObject_whenPostUser_thenStatus400() throws Exception {

        // given - precondition or setup
        mockMvc.perform(post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)));

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)));

        // then - verify the result or output using assert statements
        response.andExpect(status().isBadRequest())
            .andDo(print())
            .andExpect(jsonPath("$.message", is("El correo electrónico ya ha sido registrado")));

    }

    @DisplayName("[Integration] GET / - Success")
    @Test
    public void givenUsersList_whenGetAllUsers_thenReturnUsersList() throws Exception {

        // given - precondition or setup
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(User.builder()
            .name("John Doe")
            .email("john@doe")
            .password("123456")
            .build());
        repository.saveAll(userList);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/"));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.users.size()", is(2)));

    }

    @DisplayName("[Integration] GET / - Empty")
    @Test
    public void givenEmptyUsersList_whenGetAllUsers_thenReturnEmptyList() throws Exception {

        // given - precondition or setup

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/"));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.users.size()", is(0)));

    }

    @DisplayName("[Integration] GET /{id} - Success")
    @Test
    public void givenUserId_whenGetUserById_thenReturnUserObject() throws Exception {

        // given - precondition or setup
        User userDb = repository.save(user);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/{id}", userDb.getId()));

        // then - verify the result or output using assert statements
        response.andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.name", is(user.getName())))
            .andExpect(jsonPath("$.email", is(user.getEmail())));

    }

    @DisplayName("[Integration] GET /{id} - Failure")
    @Test
    public void givenUserId_whenGetUserById_thenReturnEmpty() throws Exception {

        // given - precondition or setup
        User userDb = repository.save(user);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/{id}", userDb.getId() + 1));

        // then - verify the result or output using assert statements
        response.andExpect(status().isNotFound())
            .andDo(print());

    }

    @DisplayName("[Integration] PUT /{id} - Success")
    @Test
    public void givenUserIdAndUpdatedUser_whenUpdateUser_thenReturnUpdatedUser() throws Exception {

        // given - precondition or setup
        User userDb = repository.save(user);
        userDb.setName("John Doe");
        userDb.setEmail("john@doe");

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/{id}", userDb.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDb)));

        // then - verify the result or output using assert statements
        response.andExpect(status().isCreated())
            .andDo(print())
            .andExpect(jsonPath("$.name", is(userDb.getName())))
            .andExpect(jsonPath("$.email", is(userDb.getEmail())));

    }

    @DisplayName("[Integration] PUT /{id} - Failure (User not found)")
    @Test
    public void givenUserIdAndUpdatedUser_whenUpdateUser_thenReturnEmpty() throws Exception {

        // given - precondition or setup
        User userDb = repository.save(user);
        userDb.setName("John Doe");
        userDb.setEmail("john@doe");

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/{id}", userDb.getId() + 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userDb)));

        // then - verify the result or output using assert statements
        response.andExpect(status().isNotFound())
            .andDo(print());

    }

}
