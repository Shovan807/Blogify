package com.ShovanParia.Blogify.Advice;

import com.ShovanParia.Blogify.ExceptionHandling.CategoryNotFoundException;
import com.ShovanParia.Blogify.ExceptionHandling.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(
            UserNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    // Handle Category Not Found
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(
            CategoryNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }



}