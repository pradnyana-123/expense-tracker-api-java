package com.example.spring_expense.controller;

import com.example.spring_expense.dto.CreateCategoryRequest;
import com.example.spring_expense.dto.UpdateCategoryRequest;
import com.example.spring_expense.dto.WebResponse;
import com.example.spring_expense.entity.Category;
import com.example.spring_expense.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping(path = "/api/categories/{userId}")
    public WebResponse<String> create(@PathVariable Long userId, @RequestBody CreateCategoryRequest request) {
       categoryService.createCategory(request, userId);

       return WebResponse.<String>builder().status(201).message("Category created successfully").build();
    }

    @GetMapping(path = "/api/categories/{userId}")
    public WebResponse<Category> get(@RequestParam String name, @PathVariable Long userId) {
        Category category = categoryService.getCategory(name, userId);

        return WebResponse.<Category>builder().status(201).message("Category retrieved successfully").data(category).build();
    }

    @PatchMapping(path = "/api/categories/{userId}/{categoryId}")
    public WebResponse<String> update(@RequestBody UpdateCategoryRequest request, @PathVariable Long userId,  @PathVariable Long categoryId) {
        categoryService.updateCategory(categoryId, request, userId);

        return WebResponse.<String>builder().message("Category updated successfully").status(200).build();
    }


    @DeleteMapping(path = "/api/categories/{userId}/{categoryId}")
    public WebResponse<String> delete(@PathVariable Long userId,  @PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId, userId);

        return WebResponse.<String>builder().message("Category deleted successfully").status(200).build();
    }

}
