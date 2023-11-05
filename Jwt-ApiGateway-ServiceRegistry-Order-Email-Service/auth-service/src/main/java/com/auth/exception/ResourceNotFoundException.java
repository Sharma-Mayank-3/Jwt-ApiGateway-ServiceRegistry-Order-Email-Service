package com.auth.exception;

public class ResourceNotFoundException extends RuntimeException{

    private String resource;
    private String resourceType;
    private int resourceParam;

    public ResourceNotFoundException(String resource, String resourceType, int resourceParam) {
        super(String.format("%s not found with %s : %s", resource, resourceType, resourceParam));
        this.resource = resource;
        this.resourceType = resourceType;
        this.resourceParam = resourceParam;
    }
}
