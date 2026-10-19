package com.ShovanParia.Blogify.ExceptionHandling;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }
}