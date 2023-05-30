package com.example.ImageService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Map<String, String>> handleCustomException(CustomException err){
        Map<String, String> errorMessage = new HashMap<>();
        errorMessage.put("message", err.getMessage());
        return new ResponseEntity<>(errorMessage, err.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUnwantedException(Exception e) {
        e.printStackTrace();
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("message","Có lỗi xảy ra, vui lòng thử lại sau");
        return  errorMap;
    }
}


