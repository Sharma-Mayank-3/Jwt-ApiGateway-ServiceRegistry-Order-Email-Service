package com.apigateway.exception;

public class JwtException extends RuntimeException{

    private String text;

    public JwtException(String text){
        super(String.format("%s not found ", text));
        this.text = text;
    }
}
