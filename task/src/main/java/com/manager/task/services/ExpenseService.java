package com.manager.task.services;

import com.manager.task.dtos.ExpenseRequest;
import com.manager.task.dtos.ExpenseResponse;
import com.manager.task.entities.Category;
import com.manager.task.entities.Expense;
import com.manager.task.entities.User;
import com.manager.task.exceptions.CategoryNotFoundException;
import com.manager.task.exceptions.ExpenseNotFoundException;
import com.manager.task.repositories.CategoryRepository;
import com.manager.task.repositories.ExpenseRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExpenseService {

        private ExpenseRepository expenseRepository;
        private CategoryRepository categoryRepository;
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

        public ExpenseResponse mapToResponse(Expense expense){
           return new ExpenseResponse(expense.getId(),expense.getAmount(),expense.getDate(),expense.getDescription(),expense.getCategory().getName());
        }

        public ExpenseResponse mapToExpenseResponse(Expense expense){
            return new ExpenseResponse(expense.getId(),expense.getAmount(),expense.getDate(),expense.getDescription(),expense.getCategory().getName());
        }

        public void addExpense(ExpenseRequest expenseRequest){
            Expense expense = mapToExpense(expenseRequest);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication(); // getting the current logged-in user from securityContext
            User user = (User) auth.getPrincipal();   // logged-in user
            expense.setUser(user); // tie expense to this current logged-in user

            expenseRepository.save(expense);
        }

        public Page<ExpenseResponse> getMyExpenses(Long categoryId, Pageable pageable) {
              Page<Expense> expenses;
              Authentication auth = SecurityContextHolder.getContext().getAuthentication();
              User user = (User) auth.getPrincipal();
              if (categoryId != null) expenses = expenseRepository.findByUserIdAndCategoryId(user.getId(), categoryId, pageable);
              else expenses = expenseRepository.findByUserId(user.getId(),pageable);

            return expenses.map(this::mapToResponse);

         }

        public void updateExpense(Long id ,ExpenseRequest expenseRequest){
            expenseRepository.findById(id).map(expense -> {
                Category category = categoryRepository.findById(expenseRequest.getCategoryId())
                        .orElseThrow(() -> new CategoryNotFoundException(expenseRequest.getCategoryId()));
                expense.setDate(expenseRequest.getDate());
                expense.setDescription(expenseRequest.getDescription());
                expense.setAmount(expenseRequest.getAmount());
                expense.setCategory(category);

                return expenseRepository.save(expense);
            }).orElseThrow(() -> new ExpenseNotFoundException(id));
        }

        public void deleteExpense(Long id){
             expenseRepository.deleteById(id);
        }

        public ExpenseResponse getExpenseById(Long id){
         Expense expense =  expenseRepository.findById(id).orElseThrow(()-> new ExpenseNotFoundException(id));
            return mapToResponse(expense);
        }

}
