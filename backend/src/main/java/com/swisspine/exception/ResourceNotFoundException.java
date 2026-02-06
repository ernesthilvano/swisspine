package com.swisspine.exception;

/**
 * Exception thrown when a requested resource is not found.
 * Results in HTTP 404 response.
 * 
 * @author SwissPine Engineering Team
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException forId(String resourceType, Long id) {
        return new ResourceNotFoundException(
                String.format("%s with ID %d not found", resourceType, id));
    }
}
