package com.example.spring_expense.controller;

import com.example.spring_expense.dto.CreateExpenseRequest;
import com.example.spring_expense.dto.UpdateExpenseRequest;
import com.example.spring_expense.dto.WebResponse;
import com.example.spring_expense.entity.Expense;
import com.example.spring_expense.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping(path = "/api/expenses/{userId}")
    public WebResponse<String> create(@RequestBody CreateExpenseRequest request, @PathVariable Long userId) {
        expenseService.createExpense(request, userId);

        return WebResponse.<String>builder().message("Expense created successfully").status(201).build();
    }

    @GetMapping(path = "/api/expenses/{userId}")
    public WebResponse<List<Expense>> getAllByUserId(@PathVariable Long userId) {
        List<Expense> allExpenses = expenseService.getAllExpenses(userId);

        return WebResponse.<List<Expense>>builder().status(200).message("All expenses retrieved").data(allExpenses).build();
    }

    @PatchMapping(path = "/api/expenses/{userId}/{expenseId}")
    public WebResponse<String> update(@PathVariable Long userId, @PathVariable Long expenseId, UpdateExpenseRequest request) {
        expenseService.updateExpense(request, expenseId, userId);

       return WebResponse.<String>builder().message("Expense updated successfully").status(200).build() ;
    }
}
