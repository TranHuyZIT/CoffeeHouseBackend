package com.tma.coffeehouse.ExceptionHandling;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomException extends RuntimeException{
    private String message;
    private HttpStatus statusCode;
}
