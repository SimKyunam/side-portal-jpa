package com.mile.portal.config.exception.exceptions;

/**
 * 405 - Method Not Allowed
 */
public class MethodNotAllowedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MethodNotAllowedException() {
        super("Method is not valid for this endpoint.");
    }

    public MethodNotAllowedException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public MethodNotAllowedException(String arg0) {
        super(arg0);
    }

    public MethodNotAllowedException(Throwable arg0) {
        super(arg0);
    }
}
