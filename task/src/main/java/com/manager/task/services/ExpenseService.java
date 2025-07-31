package com.manager.task.services;

import com.manager.task.dtos.ExpenseRequest;
import com.manager.task.dtos.ExpenseResponse;
import com.manager.task.entities.Category;
import com.manager.task.entities.Expense;
import com.manager.task.exceptions.ExpenseNotFoundException;
import com.manager.task.repositories.ExpenseRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExpenseService {

        private ExpenseRepository expenseRepository;

        public Expense mapToExpense(ExpenseRequest expenseRequest){
            Expense exp = new Expense();
            Category cat = new Category();
            cat.setId(expenseRequest.getCategoryId());
            exp.setAmount(expenseRequest.getAmount());
            exp.setDate(expenseRequest.getDate());
            exp.setCategory(cat);
            exp.setDescription(expenseRequest.getDescription());
            return exp;
        }

        public ExpenseResponse mapToExpenseResponse(Expense expense){
            return new ExpenseResponse(expense.getId(),expense.getAmount(),expense.getDate(),expense.getDescription(),expense.getCategory().getName());
        }

        public void addExpense(ExpenseRequest expenseRequest){
            expenseRepository.save(mapToExpense(expenseRequest));
        }

         public Page<ExpenseResponse> getAllExpenses(Pageable pageable){
             return expenseRepository.findAll(pageable).map(this::mapToExpenseResponse);
         }

        public void updateExpense(Long id ,ExpenseRequest expenseRequest){
            expenseRepository.findById(id).map(expense -> {
                Category cat = new Category();
                cat.setId(expenseRequest.getCategoryId());
                expense.setDate(expenseRequest.getDate());
                expense.setDescription(expenseRequest.getDescription());
                expense.setAmount(expenseRequest.getAmount());
                expense.setCategory(cat);

               return expenseRepository.save(expense);
            }).orElseThrow(()-> new ExpenseNotFoundException(id));
        }

        public void deleteExpense(Long id){
             expenseRepository.deleteById(id);
        }

        public Expense getExpenseById(Long id){
           return expenseRepository.findById(id).orElseThrow(()-> new ExpenseNotFoundException(id));
        }
}
