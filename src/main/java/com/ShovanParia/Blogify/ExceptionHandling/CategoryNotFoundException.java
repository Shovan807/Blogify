package com.ShovanParia.Blogify.ExceptionHandling;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
