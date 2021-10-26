package com.mile.portal.config.exception.exceptions;

/**
 * 401 - Unauthorized
 */
public class UnauthorizedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnauthorizedException() {
        super("User must authenticate before making a request.");
    }

    public UnauthorizedException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public UnauthorizedException(String arg0) {
        super(arg0);
    }

    public UnauthorizedException(Throwable arg0) {
        super(arg0);
    }
}
