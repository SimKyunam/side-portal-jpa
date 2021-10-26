package com.mile.portal.config.exception.exceptions;

/**
 * 500 - Internal Server Error
 */
public class InternalServerErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InternalServerErrorException() {
        super();
    }

    public InternalServerErrorException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public InternalServerErrorException(String arg0) {
        super(arg0);
    }

    public InternalServerErrorException(Throwable arg0) {
        super(arg0);
    }
}
