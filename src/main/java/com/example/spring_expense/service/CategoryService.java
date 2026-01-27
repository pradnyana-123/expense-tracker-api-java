package com.example.spring_expense.service;

import com.example.spring_expense.dto.CreateCategoryRequest;
import com.example.spring_expense.dto.UpdateCategoryRequest;
import com.example.spring_expense.entity.Category;
import com.example.spring_expense.entity.User;
import com.example.spring_expense.repository.CategoryRepository;
import com.example.spring_expense.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CategoryService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createCategory(CreateCategoryRequest request, Long userId) {
        this.validationService.validate(request);

        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        boolean categoryExists = categoryRepository.existsByName(request.getName());
        if(categoryExists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category already exists");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setUser(user);

        categoryRepository.save(category);
    }

    @Transactional(readOnly = true)
    public Category getCategory(String categoryName, Long userId) {
        Category category = categoryRepository.findByName(categoryName).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if(!category.getUser().getId().equals(userId)) {
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not the owner of this category") ;
        }

        return category;
    }

    @Transactional
    public void updateCategory(Long categoryId, UpdateCategoryRequest request, Long userId) {
        validationService.validate(request);

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if(!category.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to update this category");
        }

        category.setName(request.getName());

        categoryRepository.save(category);
    }


    @Transactional
    public void deleteCategory(Long categoryId, Long userId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));

        if(!category.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to update this category");
        }

        categoryRepository.delete(category);
    }

}
