package com.storeops.common.errors;

public abstract class AppError extends RuntimeException {

    private final String code;
    private final int statusCode;

    protected AppError(final String code, final String message, final int statusCode) {
        super(message);
        this.code = code;
        this.statusCode = statusCode;
    }

    public String getCode() {
        return code;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
