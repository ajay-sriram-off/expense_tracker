package com.manager.task.controller;

import com.manager.task.dtos.ExpenseRequest;
import com.manager.task.dtos.ExpenseResponse;
import com.manager.task.entities.Expense;
import com.manager.task.services.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expense")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Void> addExpense(@Valid @RequestBody ExpenseRequest request) {
        expenseService.addExpense(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getAllExpenses() {
        return ResponseEntity.ok(expenseService.getAllExpenses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(Long id){
        Expense expense = expenseService.getExpenseById(id);
        ExpenseResponse expenseResponse = new ExpenseResponse(expense.getId() ,expense.getAmount() ,expense.getDate() , expense.getDescription() ,expense.getCategory().getName());
        return ResponseEntity.ok(expenseResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateExpense(@PathVariable Long id ,@RequestBody ExpenseRequest expenseRequest ){
        expenseService.updateExpense(id ,expenseRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(Long id){
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }


}
