package com.example.spring_expense.service;

import com.example.spring_expense.dto.CreateExpenseRequest;
import com.example.spring_expense.dto.UpdateExpenseRequest;
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

import java.util.List;

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

    public List<Expense> getAllExpenses(Long userId) {
        if(!userRepository.existsById(userId)) {
           throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID: " + userId + "not found");
        }

        return expenseRepository.findByUserId(userId);
    }

    public void updateExpense(UpdateExpenseRequest request, Long expenseId, Long userId) {
        validationService.validate(request);

        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));

        if(!expense.getUser().getId().equals(userId)) {
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to update this expense");
        }

        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());

        expenseRepository.save(expense);
    }

    public void deleteExpense(Long userId, Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Expense not found"));

        if(!expense.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You are not allowed to delete this expense");
        }

        expenseRepository.delete(expense);
    }



}
