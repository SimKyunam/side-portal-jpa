package com.mile.portal.config.exception.exceptions;

/**
 * 403 - Forbidden
 */
public class ForbiddenException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ForbiddenException() {
        super("Policy does not allow current user to do this operation.");
    }

    public ForbiddenException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ForbiddenException(String arg0) {
        super(arg0);
    }

    public ForbiddenException(Throwable arg0) {
        super(arg0);
    }
}
