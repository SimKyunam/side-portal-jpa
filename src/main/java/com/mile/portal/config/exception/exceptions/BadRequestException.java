package com.mile.portal.config.exception.exceptions;

/**
 * 400 - Bad Request
 */
public class BadRequestException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super("Some content in the request was invalid.");
    }

    public BadRequestException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public BadRequestException(String arg0) {
        super(arg0);
    }

    public BadRequestException(Throwable arg0) {
        super(arg0);
    }
}

