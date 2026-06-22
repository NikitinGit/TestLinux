package com.example.testlinux.exceptions;

public class ApiValidationException extends ValidationException {

    private final String errorCode;

    public ApiValidationException(String message) {
        this(message, null);
    }

    public ApiValidationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

