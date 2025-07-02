package com.example.restaurantmanagement.infrastructure.exception;

public class BaseException extends RuntimeException {
    private Object[] args;

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(String message, Object[] args) {
        super(message);
        this.args = args;
    }

    public BaseException(String message, Throwable cause, Object[] args) {
        super(message, cause);
        this.args = args;
    }

    public Object[] getArgs() {
        return args;
    }
}

