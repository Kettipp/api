package com.my.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.api.model.User;
import com.my.api.repository.impl.UserRepositoryImpl;
import com.my.api.service.UserService;
import com.my.api.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest({UserController.class, UserServiceImpl.class, UserRepositoryImpl.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void AddUserPositiveTest() throws Exception {
        User user = User.builder().build();

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("The user has been created"))
                .andExpect(jsonPath("$.id").value(1L))
                .andDo(print());
    }

    @Test
    void AddUserNegativeTest() throws Exception {
        User user = User.builder().email("test@com.ua").build();
        userService.addUser(user);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("User already exist"))
                .andDo(print());
    }

    @Test
    void UpdateAllUserFieldsPositiveTest() throws Exception {
        User orig = builder().email("test@mail.com").build();
        User updated = builder().email("updated@mail.com").build();

        long id = userService.addUser(orig);

        mockMvc.perform(put("/api/v1/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User successfully updated"))
                .andExpect(jsonPath("$.user.id").value(id))
                .andDo(print());
    }

    @Test
    void UpdateAllUserFieldsNegativeTest() throws Exception {
        User updated = builder().email("updated@mail.com").build();

        long id = 1L;

        mockMvc.perform(put("/api/v1/users/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"))
                .andDo(print());
    }
    @Test
    void UpdateSomeOfUserFieldsPositiveTest() throws Exception {
        User user = builder().build();
        long id = userService.addUser(user);
        String email = "newemail@example.com";
        Map<String, Object> updates = new HashMap<>();
        updates.put("email", email);

        mockMvc.perform(patch("/api/v1/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User successfully updated"))
                .andExpect(jsonPath("$.user.email").value(email))
                .andExpect(jsonPath("$.user.id").value(id))
                .andDo(print());
    }

    @Test
    void UpdateSomeOfUserFieldsNegativeTest() throws Exception {
        String email = "newemail@example.com";
        Map<String, Object> updates = new HashMap<>();
        updates.put("email", email);

        mockMvc.perform(patch("/api/v1/users/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"))
                .andDo(print());
    }

    @Test
    void DeleteUserPositiveTest() throws Exception {
        long userId = 1L;
        userService.addUser(User.builder().id(userId).birthDate(LocalDate.of(1999, 1, 1)).build());
        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User successfully deleted"))
                .andDo(print());
    }

    @Test
    void DeleteUserNegativeTest() throws Exception {
        long userId = 1L;
        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"))
                .andDo(print());
    }

    @Test
    void GetUsersByBirthDatePositiveTest() throws Exception {
        LocalDate start = LocalDate.of(1997, 1, 1);
        LocalDate end = LocalDate.of(2011, 12, 31);
        userService.addUser(User.builder().birthDate(LocalDate.of(1998, 1, 1)).build());

        mockMvc.perform(get("/api/v1/users/filtered")
                        .param("start", start.toString())
                        .param("end", end.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users.length()").value(1))
                .andDo(print());
    }

    @Test
    void GetUsersByBirthDateNegativeTest() throws Exception {
        LocalDate start = LocalDate.of(2012, 1, 1);
        LocalDate end = LocalDate.of(2011, 12, 31);

        mockMvc.perform(get("/api/v1/users/filtered")
                        .param("start", start.toString())
                        .param("end", end.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Start date should be greater  then end date"))
                .andDo(print());
    }

    private User.UserBuilder builder() {
        return User.builder()
                .firstName("user")
                .email("test@mail.com")
                .lastName("user")
                .birthDate(LocalDate.of(1999, 10, 26))
                .address("Kharkiv")
                .phoneNumber("0990675224");
    }
}
