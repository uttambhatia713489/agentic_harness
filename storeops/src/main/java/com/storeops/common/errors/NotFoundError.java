package com.storeops.common.errors;

public class NotFoundError extends AppError {

    public NotFoundError(final String message) {
        super("NOT_FOUND", message, 404);
    }
}
