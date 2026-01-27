package com.example.spring_expense.controller;

import com.example.spring_expense.dto.RegisterUserRequest;
import com.example.spring_expense.entity.User;
import com.example.spring_expense.repository.CategoryRepository;
import com.example.spring_expense.repository.ExpenseRepository;
import com.example.spring_expense.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        expenseRepository.deleteAll();
        categoryRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void TestRegisterSuccess() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("Andika");
        request.setPassword("pradnyana123");

        mockMvc.perform(
            post("/api/users")
                    .with(csrf())
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        );
    }


    @Test
    void TestRegisterBadRequest() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("");
        request.setPassword("");

        mockMvc.perform(
                post("/api/users")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void TestRegisterUserAlreadyExists() throws Exception {
        User user = new User();
        user.setUsername("Budi");
        user.setPassword("budi123");

        userRepository.save(user);

        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("Budi");
        request.setPassword("budi123");

        mockMvc.perform(
                post("/api/users")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isBadRequest()
        );
    }

    @Test
    void testLoginSuccess() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        mockMvc.perform(post("/login")
                        .with(csrf())
                .param("username", "test")
                .param("password", "test123")

        ).andExpectAll(status().isOk());
    }

    @Test
    void testGetUserSuccess() throws Exception {
        User user = new User();
        user.setUsername("Andika");
        user.setPassword(encoder.encode("pradnyana123"));

        userRepository.save(user);

        mockMvc.perform(get("/api/users/" + user.getId().toString())
                .with(csrf())
        ).andExpectAll(status().isOk(), jsonPath("$.data.username").value("Andika"));
    }

    @Test
    void testGetUserFailed() throws Exception {
        User user = new User();
        user.setUsername("Andika");
        user.setPassword(encoder.encode("pradnyana123"));

        userRepository.save(user);

        String wrongId = String.valueOf(Long.MAX_VALUE);

        mockMvc.perform(get("/api/users/" + wrongId)
                .with(csrf())
        ).andExpectAll(status().isNotFound());
    }

    @Test
    void testGetUserNotFound() throws Exception {
        Long nonExistenId = -1L;

        mockMvc.perform(get("/api/users/" + nonExistenId.toString())
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(status().isNotFound());
    }

    @Test
    void testGetUserWrongIdFormat() throws Exception {
        mockMvc.perform(get("/api/users/wrongId")
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
        ).andExpectAll(status().isBadRequest());
    }

}
