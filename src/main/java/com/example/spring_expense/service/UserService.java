package com.example.spring_expense.service;

import com.example.spring_expense.dto.RegisterUserRequest;
import com.example.spring_expense.entity.User;
import com.example.spring_expense.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public PasswordEncoder encoder;

    @Autowired
    private ValidationService validationService;

    public void registerUser(RegisterUserRequest request) {
        validationService.validate(request);

        boolean exists = userRepository.existsByUsername(request.getUsername());

        if(exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }

        String hashedPassword = encoder.encode(request.getPassword());

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(hashedPassword);

        userRepository.save(user);
    }

    public User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }
}
