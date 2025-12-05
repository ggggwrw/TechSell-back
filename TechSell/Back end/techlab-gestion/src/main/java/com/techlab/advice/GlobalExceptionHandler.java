package com.techlab.advice;

import com.techlab.excepciones.NotFoundException;
import com.techlab.excepciones.StockInsuficienteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String ERROR_KEY = "error";
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NotFoundException ex) {
        Map<String, String> body = new HashMap<>();
        body.put(ERROR_KEY, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StockInsuficienteException.class)
    public ResponseEntity<Map<String, String>> handleStock(StockInsuficienteException ex) {
        Map<String, String> body = new HashMap<>();
        body.put(ERROR_KEY, ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> body = new HashMap<>();
        String message = "Datos inválidos";
        if (ex.getBindingResult() != null) {
            var fieldError = ex.getBindingResult().getFieldError();
            if (fieldError != null && fieldError.getDefaultMessage() != null) {
                message = fieldError.getDefaultMessage();
            }
        }
        body.put(ERROR_KEY, message);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}
