package com.bankingsystem.bankapp.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            AccountNotFoundException.class,
            UserAlreadyExistsException.class,
            InsufficientFundsException.class,
            InvalidAmountException.class
    })
    public ResponseEntity<String> handleCustomExceptions(RuntimeException ex) {
        HttpStatus status = ex instanceof AccountNotFoundException
                ? HttpStatus.NOT_FOUND
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

}