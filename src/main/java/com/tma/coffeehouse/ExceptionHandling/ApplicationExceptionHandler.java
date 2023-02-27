package com.tma.coffeehouse.ExceptionHandling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleAuthenticationException(AuthenticationException err){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", err.getMessage());
        return errorMap;
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleCustomException(CustomException err){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", err.getMessage());
        return new ResponseEntity<>(errorMap, err.getStatusCode());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException err){
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message", err.getMessage());
        return errorMap;
    }


}
