package com.example.spring_expense.service;

import com.example.spring_expense.dto.CreateExpenseRequest;
import com.example.spring_expense.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private ValidationService validationService;

    public void createExpense(CreateExpenseRequest request, Long userId) {
        validationService.validate(request);



    }


}
