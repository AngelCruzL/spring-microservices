package dev.angelcruzl.msvc.users.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.angelcruzl.msvc.users.models.entities.User;
import dev.angelcruzl.msvc.users.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    public void setUp() {
        user = User.builder()
            .id(1L)
            .name("Ángel Cruz")
            .email("me@angelcruzl.dev")
            .password("123456")
            .build();
    }

    @DisplayName("POST / - Success")
    @Test
    public void givenUserObject_whenPostUser_thenStatus201() throws Exception {

        // given
        given(service.save(any(User.class)))
            .willAnswer(invocation -> invocation.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)));

        // then
        response.andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is(user.getName())))
            .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @DisplayName("POST / - Failure")
    @Test
    public void givenInvalidUserObject_whenPostUser_thenStatus400() throws Exception {

        // given
        given(service.existsByEmail("me@angelcruzl.dev")).willReturn(true);

        // when
        ResultActions response = mockMvc.perform(post("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)));

        // then
        response.andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("El correo electrónico ya ha sido registrado"));

    }

    @DisplayName("GET / - Success")
    @Test
    public void givenUsersList_whenGetAllUsers_thenStatus200() throws Exception {

        // given
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(User.builder()
            .name("John Doe")
            .email("john@doe")
            .password("123456")
            .build());

        given(service.findAll()).willReturn(userList);

        // when
        ResultActions response = mockMvc.perform(get("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)));

        // then
        response.andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.users.size()", is(userList.size())));

    }

    @DisplayName("GET / - Empty")
    @Test
    public void givenEmptyUsersList_whenGetAllUsers_thenStatus200() throws Exception {

        // given
        List<User> userList = new ArrayList<>();
        given(service.findAll()).willReturn(userList);

        // when
        ResultActions response = mockMvc.perform(get("/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)));

        // then
        response.andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.users.size()", is(0)));

    }

    @DisplayName("GET /{id} - Success")
    @Test
    public void givenUserId_whenGetUser_thenStatus200() throws Exception {

        // given
        given(service.findById(user.getId())).willReturn(Optional.of(user));

        // when
        ResultActions response = mockMvc.perform(get("/{id}", user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)));

        // then
        response.andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.name", is(user.getName())))
            .andExpect(jsonPath("$.email", is(user.getEmail())));

    }

    @DisplayName("GET /{id} - Failure")
    @Test
    public void givenInvalidUserId_whenGetUser_thenStatus404() throws Exception {

        // given
        given(service.findById(user.getId())).willReturn(Optional.empty());

        // when
        ResultActions response = mockMvc.perform(get("/{id}", user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)));

        // then
        response.andExpect(status().isNotFound());

    }

    @DisplayName("PUT /{id} - Success")
    @Test
    public void givenUserObject_whenPutUser_thenStatus201() throws Exception {

        // given
        User updatedUser = User.builder()
            .id(user.getId())
            .name("Luis Lara")
            .email(user.getEmail())
            .password(user.getPassword())
            .build();

        given(service.findById(user.getId())).willReturn(Optional.of(user));
        given(service.save(any(User.class)))
            .willAnswer(invocation -> invocation.getArgument(0));

        // when
        ResultActions response = mockMvc.perform(put("/{id}", user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updatedUser)));

        // then
        response.andExpect(status().isCreated())
            .andDo(print())
            .andExpect(jsonPath("$.name", is(updatedUser.getName())))
            .andExpect(jsonPath("$.email", is(updatedUser.getEmail())));

    }

    @DisplayName("PUT /{id} - Failure")
    @Test
    public void givenInvalidUserObject_whenPutUser_thenStatus400() throws Exception {

        // given
        given(service.findById(user.getId())).willReturn(Optional.of(user));
        given(service.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        // when
        ResultActions response = mockMvc.perform(put("/{id}", user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)));

        // then
        response.andExpect(status().isBadRequest())
            .andDo(print())
            .andExpect(jsonPath("$.message").value("El correo electrónico ya ha sido registrado"));

    }

    @DisplayName("DELETE /{id} - Failure")
    @Test
    public void givenInvalidUserId_whenDeleteUser_thenStatus404() throws Exception {

        // given
        given(service.findById(user.getId())).willReturn(Optional.empty());

        // when
        ResultActions response = mockMvc.perform(delete("/{id}", user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)));

        // then
        response.andExpect(status().isNotFound());

    }

    @DisplayName("DELETE /{id} - Success")
    @Test
    public void givenUserId_whenDeleteUser_thenStatus204() throws Exception {

        // given
        given(service.findById(user.getId())).willReturn(Optional.of(user));

        // when
        ResultActions response = mockMvc.perform(delete("/{id}", user.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)));

        // then
        response.andExpect(status().isNoContent());

    }

    @DisplayName("GET /users-by-course - Success")
    @Test
    public void givenCourseIds_whenGetUsersByCourse_thenStatus200() throws Exception {

        // given
        List<Long> ids = List.of(1L, 2L, 3L);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(User.builder()
            .id(2L)
            .name("John Doe")
            .email("john@doe")
            .password("123456")
            .build());
        userList.add(User.builder()
            .id(3L)
            .name("Jane Doe")
            .email("jane@doe")
            .password("123456")
            .build());

        given(service.listByIds(ids)).willReturn(userList);

        // when
        ResultActions response = mockMvc.perform(get("/users-by-course")
            .param("ids", "1", "2", "3")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(ids)));

        // then
        response.andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$.size()", is(userList.size())));

    }

}
