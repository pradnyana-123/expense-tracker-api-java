package com.example.spring_expense.controller;

import com.example.spring_expense.dto.CreateCategoryRequest;
import com.example.spring_expense.dto.UpdateCategoryRequest;
import com.example.spring_expense.entity.Category;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

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
    void testCreateCategorySuccess() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        String userId = user.getId().toString();

        CreateCategoryRequest request = new CreateCategoryRequest();

        request.setName("Jajan");

        mockMvc.perform(post("/api/categories/" + userId)
                .with(csrf())
                .with(user("test").password("test123"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isOk());
    }

    @Test
    void testCreateCategoryUserNotFound() throws Exception {
//        User user = new User();
//        user.setUsername("test");
//        user.setPassword(encoder.encode("test123"));
//
//        userRepository.save(user);
//
//        String userId = user.getId().toString();

        CreateCategoryRequest request = new CreateCategoryRequest();

        request.setName("Jajan");

        mockMvc.perform(post("/api/categories/123" )
                .with(csrf())
                .with(user("test").password("test123"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isNotFound());
    }

    @Test
    void testCreateCategoryInvalidData() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        String userId = user.getId().toString();

        CreateCategoryRequest request = new CreateCategoryRequest();

        request.setName("");

        mockMvc.perform(post("/api/categories/" + userId)
                .with(csrf())
                .with(user("test").password("test123"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isBadRequest());
    }


    @Test
    void testCreateCategoryAlreadyExists() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        Category category = new Category();
        category.setName("Jajan");
        category.setUser(user);

        categoryRepository.save(category);

        String userId = user.getId().toString();

        CreateCategoryRequest request = new CreateCategoryRequest();

        request.setName("Jajan");

        mockMvc.perform(post("/api/categories/" + userId)
                .with(csrf())
                .with(user("test").password("test123"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isBadRequest());
    }

    @Test
    void testGetCategorySuccess() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        Category category = new Category();
        category.setName("Groceries");
        category.setUser(user);

        categoryRepository.save(category);

        String userId = user.getId().toString();


        mockMvc.perform(get("/api/categories/{userId}", userId)
                .param("name", "Groceries")
                .with(csrf())
                .with(user("test").password("test123"))
        ).andExpectAll(status().isOk());
    }

    @Test
    void testGetCategoryNotFound() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        String userId = user.getId().toString();

        mockMvc.perform(get("/api/categories/{userId}", userId)
                .param("name", "Groceries")
                .with(csrf())
                .with(user("test").password("test123"))
        ).andExpectAll(status().isNotFound());
    }

    @Test
    void testGetCategoryUserUnauthorized() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        Category category = new Category();
        category.setName("Groceries");
        category.setUser(user);

        categoryRepository.save(category);

        Long userId = user.getId() + 2 ;

        mockMvc.perform(get("/api/categories/{userId}", userId.toString())
                .param("name", "Groceries")
                .with(csrf())
                .with(user("test").password("test123"))
        ).andExpectAll(status().isUnauthorized());
    }

    @Test
    void testUpdateCategorySuccess() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        Category category = new Category();
        category.setName("Groceries");
        category.setUser(user);

        categoryRepository.save(category);

        UpdateCategoryRequest request = new UpdateCategoryRequest();

        request.setName("Common");

        String userId = user.getId().toString();
        String categoryId = category.getId().toString();

        mockMvc.perform(patch("/api/categories/{userId}/{categoryId}", userId, categoryId)
                .with(csrf())
                .with(user("test").password("test123"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isOk());
    }

    @Test
    void testUpdateCategoryInvalidData() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        Category category = new Category();
        category.setName("Groceries");
        category.setUser(user);

        categoryRepository.save(category);

        UpdateCategoryRequest request = new UpdateCategoryRequest();

        request.setName("");

        Long userId = user.getId() + 2;
        String categoryId = category.getId().toString();

        mockMvc.perform(patch("/api/categories/{userId}/{categoryId}", userId.toString(), categoryId)
                .with(csrf())
                .with(user("test").password("test123"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isBadRequest());
    }

    @Test
    void testUpdateCategoryUserUnauthorized() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        Category category = new Category();
        category.setName("Groceries");
        category.setUser(user);

        categoryRepository.save(category);

        UpdateCategoryRequest request = new UpdateCategoryRequest();

        request.setName("");

        String userId = user.getId().toString();
        String categoryId = category.getId().toString();

        mockMvc.perform(patch("/api/categories/{userId}/{categoryId}", userId, categoryId)
                .with(csrf())
                .with(user("test").password("test123"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isBadRequest());
    }

    @Test
    void testUpdateCategoryNotFound() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        UpdateCategoryRequest request = new UpdateCategoryRequest();

        request.setName("Groceries");

        String userId = user.getId().toString();
        Long categoryId = Long.MAX_VALUE;

        mockMvc.perform(patch("/api/categories/{userId}/{categoryId}", userId, categoryId.toString())
                .with(csrf())
                .with(user("test").password("test123"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isNotFound());
    }

    @Test
    void testDeleteCategorySuccess() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        Category category = new Category();
        category.setName("Groceries");
        category.setUser(user);

        categoryRepository.save(category);

        String userId = user.getId().toString();
        String categoryId = category.getId().toString();

        mockMvc.perform(delete("/api/categories/{userId}/{categoryId}", userId, categoryId)
                .with(csrf())
                .with(user("test").password("test123"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(status().isOk());
    }

    @Test
    void testDeleteCategoryNotFound() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        Category category = new Category();
        category.setName("Groceries");
        category.setUser(user);

        categoryRepository.save(category);

        String userId = user.getId().toString();
        Long categoryId = category.getId() + 2;

        mockMvc.perform(delete("/api/categories/{userId}/{categoryId}", userId, categoryId.toString())
                .with(csrf())
                .with(user("test").password("test123"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(status().isNotFound());
    }

    @Test
    void testDeleteCategoryUserUnauthorized() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(encoder.encode("test123"));

        userRepository.save(user);

        Category category = new Category();
        category.setName("Groceries");
        category.setUser(user);

        categoryRepository.save(category);

        Long userId = user.getId() + 2;
        String categoryId = category.getId().toString();

        mockMvc.perform(delete("/api/categories/{userId}/{categoryId}", userId.toString(), categoryId)
                .with(csrf())
                .with(user("test").password("test123"))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(status().isUnauthorized());
    }

}
