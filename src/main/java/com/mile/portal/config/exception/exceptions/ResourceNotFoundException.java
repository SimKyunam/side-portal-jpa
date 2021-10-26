package com.mile.portal.config.exception.exceptions;

/**
 * 404 - Not Found
 */
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException() {
        super("The requested resource could not be found.");
    }

    public ResourceNotFoundException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ResourceNotFoundException(String arg0) {
        super(arg0);
    }

    public ResourceNotFoundException(Throwable arg0) {
        super(arg0);
    }
}
