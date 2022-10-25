package com.candyshop.islodycze.exceptions;

public class ApplicationException extends RuntimeException {

    public ApplicationException(final String message) {
        super(message);
    }

    public ApplicationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
