package com.reduxr.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected @Nullable ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, 
            HttpHeaders headers, 
            HttpStatusCode status, 
            WebRequest request
    ) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        List<String> errors = ex.getBindingResult()
                .getAllErrors().stream()
                .map(this::createErrorMessage)
                .toList();
        
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("errors", errors);
        return new ResponseEntity<>(responseBody, headers, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("error", ex.getMessage());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_FOUND);
    }
    
    private String createErrorMessage(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            String field = fieldError.getField();
            String message = fieldError.getDefaultMessage();
            return field + " " + message;
        }
        return null;
    }
}
