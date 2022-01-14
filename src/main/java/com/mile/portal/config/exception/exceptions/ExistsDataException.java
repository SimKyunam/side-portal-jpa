package com.mile.portal.config.exception.exceptions;

/**
 * 404 - Not Found
 */
public class ExistsDataException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExistsDataException() {
        super("Result data exists.");
    }

    public ExistsDataException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ExistsDataException(String arg0) {
        super(arg0);
    }

    public ExistsDataException(Throwable arg0) {
        super(arg0);
    }
}
