package com.example.spring_expense.controller;

import com.example.spring_expense.dto.CreateExpenseRequest;
import com.example.spring_expense.dto.WebResponse;
import com.example.spring_expense.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping(path = "/api/expenses/{userId}")
    public WebResponse<String> create(@RequestBody CreateExpenseRequest request, @PathVariable Long userId) {
        expenseService.createExpense(request, userId);

        return WebResponse.<String>builder().message("Expense created successfully").status(201).build();
    }
}
