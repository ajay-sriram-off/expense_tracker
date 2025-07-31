package com.manager.task.exceptions;

public class CategoryNotFoundException extends RuntimeException{

    public CategoryNotFoundException(Long id){
        super("Category not found for the given id "+ id);
    }
}
