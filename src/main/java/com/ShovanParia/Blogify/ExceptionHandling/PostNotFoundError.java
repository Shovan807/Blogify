package com.ShovanParia.Blogify.ExceptionHandling;

public class PostNotFoundError extends RuntimeException {
    public PostNotFoundError(String message) {
        super(message);
    }
}
