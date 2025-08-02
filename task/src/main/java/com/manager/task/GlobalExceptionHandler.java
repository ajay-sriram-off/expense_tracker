package com.manager.task;

import com.manager.task.exceptions.CategoryNotFoundException;
import com.manager.task.exceptions.ExpenseNotFoundException;
import com.manager.task.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpenseNotFoundException.class)
    public ResponseEntity<Map<String , String>> handleExpenseNotFound(ExpenseNotFoundException ex){
        Map<String,String> exceptions = new HashMap<>();
        exceptions.put("error",ex.getMessage());

        return new ResponseEntity<>(exceptions,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<Map<String , String>> handleCategoryNotFound(CategoryNotFoundException ex){
        Map<String,String> exceptions = new HashMap<>();
        exceptions.put("error",ex.getMessage());

        return new ResponseEntity<>(exceptions,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String , String>> handleUserNotFound(UserNotFoundException ex){
        Map<String , String> exceptions= new HashMap<>();
        exceptions.put("error" ,ex.getMessage());

        return new ResponseEntity<>(exceptions,HttpStatus.BAD_REQUEST);
    }
}
