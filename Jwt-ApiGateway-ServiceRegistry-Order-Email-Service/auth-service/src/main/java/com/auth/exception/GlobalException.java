package com.auth.exception;

import com.auth.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundException(ResourceNotFoundException ex){
        return new ResponseEntity<>(new ApiResponse("exception occours", false, "auth-service", ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse> jwtException(JwtException ex){
        return new ResponseEntity<>(new ApiResponse("jwt exception", false, "auth-service", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
