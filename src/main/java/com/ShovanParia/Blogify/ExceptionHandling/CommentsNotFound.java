package com.ShovanParia.Blogify.ExceptionHandling;

public class CommentsNotFound extends RuntimeException {
    public CommentsNotFound(String message) {
        super(message);
    }
}
