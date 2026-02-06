package com.swisspine.exception;

/**
 * Exception thrown when a business rule is violated.
 * Results in HTTP 400 response.
 * 
 * @author SwissPine Engineering Team
 */
public class BusinessRuleViolationException extends RuntimeException {
    public BusinessRuleViolationException(String message) {
        super(message);
    }
}
