package com.apigateway.exception;

import com.apigateway.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse> jwtException(JwtException ex){
        return new ResponseEntity<>(new ApiResponse("jwt exception", false, "auth-service", ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}
