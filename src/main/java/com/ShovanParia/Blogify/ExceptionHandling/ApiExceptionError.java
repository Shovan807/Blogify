package com.ShovanParia.Blogify.ExceptionHandling;

import org.springframework.http.HttpStatus;

public class ApiExceptionError extends RuntimeException {
    public ApiExceptionError(String message, boolean b, HttpStatus unauthorized) {
        super(message);
    }
}
