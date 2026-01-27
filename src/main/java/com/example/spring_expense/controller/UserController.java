package com.example.spring_expense.controller;

import com.example.spring_expense.dto.RegisterUserRequest;
import com.example.spring_expense.dto.WebResponse;
import com.example.spring_expense.entity.User;
import com.example.spring_expense.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.awt.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "/api/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
        userService.registerUser(request);

        return WebResponse.<String>builder().data("OK").status(201).build();
    }

    @GetMapping(path = "/api/users/{id}")
    public WebResponse<User> find(@PathVariable Long id) {
        User user = userService.findUser(id);

        return WebResponse.<User>builder().data(user).status(200).message("User retrieved").build();
    }
}
