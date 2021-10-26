package com.mile.portal.config.exception.exceptions;

/**
 * JsonConvertException class
 */
public class JsonConvertException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public JsonConvertException() {
        super();
    }

    public JsonConvertException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public JsonConvertException(String arg0) {
        super(arg0);
    }

    public JsonConvertException(Throwable arg0) {
        super(arg0);
    }
}
