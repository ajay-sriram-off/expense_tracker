package com.manager.task.exceptions;

public class ExpenseNotFoundException extends RuntimeException{

    public ExpenseNotFoundException(Long id){
        super("Expense not found for the given id "+ id);
    }
}
