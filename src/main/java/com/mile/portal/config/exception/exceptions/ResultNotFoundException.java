package com.mile.portal.config.exception.exceptions;

/**
 * 404 - Not Found
 */
public class ResultNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResultNotFoundException() {
        super("The requested result could not be found.");
    }

    public ResultNotFoundException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ResultNotFoundException(String arg0) {
        super(arg0);
    }

    public ResultNotFoundException(Throwable arg0) {
        super(arg0);
    }
}
