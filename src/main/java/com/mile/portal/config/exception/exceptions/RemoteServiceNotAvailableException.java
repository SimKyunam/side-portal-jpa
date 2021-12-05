package com.mile.portal.config.exception.exceptions;

/**
 * 403 - Forbidden
 */
public class RemoteServiceNotAvailableException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RemoteServiceNotAvailableException() {
        super("remote Service not available");
    }

    public RemoteServiceNotAvailableException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public RemoteServiceNotAvailableException(String arg0) {
        super(arg0);
    }

    public RemoteServiceNotAvailableException(Throwable arg0) {
        super(arg0);
    }
}
