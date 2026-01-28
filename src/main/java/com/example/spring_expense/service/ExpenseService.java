package com.example.spring_expense.service;

import com.example.spring_expense.dto.CreateExpenseRequest;
import com.example.spring_expense.entity.Category;
import com.example.spring_expense.entity.Expense;
import com.example.spring_expense.entity.User;
import com.example.spring_expense.repository.CategoryRepository;
import com.example.spring_expense.repository.ExpenseRepository;
import com.example.spring_expense.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    public void createExpense(CreateExpenseRequest request, Long userId) {
        validationService.validate(request);

        User user = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Expense expense = new Expense();
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setUser(user);

        expenseRepository.save(expense);
    }


}
