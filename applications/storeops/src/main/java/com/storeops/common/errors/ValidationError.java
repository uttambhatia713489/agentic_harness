package com.storeops.common.errors;

public class ValidationError extends AppError {

    public ValidationError(final String message) {
        super("VALIDATION_ERROR", message, 400);
    }
}
