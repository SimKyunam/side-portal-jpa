package com.mile.portal.config.exception.exceptions;

/**
 * UnknownErrorException class
 */
public class UnknownErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnknownErrorException() {
        super();
    }

    public UnknownErrorException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public UnknownErrorException(String arg0) {
        super(arg0);
    }

    public UnknownErrorException(Throwable arg0) {
        super(arg0);
    }
}
