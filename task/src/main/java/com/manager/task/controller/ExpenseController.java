package com.manager.task.controller;

import com.manager.task.dtos.ExpenseRequest;
import com.manager.task.dtos.ExpenseResponse;
import com.manager.task.services.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<Void> addExpense(@Valid @RequestBody ExpenseRequest request) {
        expenseService.addExpense(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long id){
        ExpenseResponse response = expenseService.getExpenseById(id);
        return ResponseEntity.ok(response);
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

    @GetMapping
    public ResponseEntity<Page<ExpenseResponse>> getMyExpenses(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(expenseService.getMyExpenses(categoryId, pageable));
    }


}
