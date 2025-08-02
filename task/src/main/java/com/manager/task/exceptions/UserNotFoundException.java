package com.manager.task.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id){
        super("User not found for the given id "+ id);
    }
}
