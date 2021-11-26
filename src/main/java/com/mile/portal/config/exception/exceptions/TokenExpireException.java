package com.mile.portal.config.exception.exceptions;

/**
 * 400 - Bad Request
 */
public class TokenExpireException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TokenExpireException() {
        super("Some content in the request was invalid.");
    }

    public TokenExpireException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public TokenExpireException(String arg0) {
        super(arg0);
    }

    public TokenExpireException(Throwable arg0) {
        super(arg0);
    }
}

