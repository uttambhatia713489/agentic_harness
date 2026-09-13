package com.storeops.common.errors;

public class ConflictError extends AppError {

    public ConflictError(final String message) {
        super("CONFLICT", message, 409);
    }
}
