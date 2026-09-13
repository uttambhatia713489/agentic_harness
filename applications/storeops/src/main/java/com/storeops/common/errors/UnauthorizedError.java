package com.storeops.common.errors;

public class UnauthorizedError extends AppError {

    public UnauthorizedError(final String message) {
        super("UNAUTHORIZED", message, 401);
    }
}
