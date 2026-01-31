package com.example.spring_expense.controller;

import com.example.spring_expense.dto.CreateExpenseRequest;
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

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ExpenseControllerTest {

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
    void testCreateExpenseSuccess() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        String userId = user.getId().toString();

        CreateExpenseRequest request = new CreateExpenseRequest();

        BigDecimal amount = new BigDecimal("200.000");

        request.setAmount(amount);
        request.setDescription("Buy some veggies, meat, and water");

        mockMvc.perform(post("/api/expenses/{userId}", userId)
                .with(csrf())
                .with(user("test").password("test123"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isOk());
    }

    @Test
    void testCreateExpenseInvalidData() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        String userId = user.getId().toString();

        CreateExpenseRequest request = new CreateExpenseRequest();

        BigDecimal amount = new BigDecimal("0");

        request.setAmount(amount);
        request.setDescription("");

        mockMvc.perform(post("/api/expenses/{userId}", userId)
                .with(csrf())
                .with(user("test").password("test123"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isBadRequest());

        request.setAmount(null);
        mockMvc.perform(post("/api/expenses/{userId}", userId)
                .with(csrf())
                .with(user("test").password("test123"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isBadRequest());
    }

    @Test
    void testCreateExpenseUserNotFound() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        String userId = user.getId().toString() + 2;

        CreateExpenseRequest request = new CreateExpenseRequest();

        BigDecimal amount = new BigDecimal("200000");

        request.setAmount(amount);
        request.setDescription("");

        mockMvc.perform(post("/api/expenses/{userId}", userId)
                .with(csrf())
                .with(user("test").password("test123"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isNotFound());
    }


}
