package com.storeops.common.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionMapper {

    @ExceptionHandler(AppError.class)
    public ResponseEntity<ErrorResponse> handleAppError(final AppError error) {
        return ResponseEntity
                .status(HttpStatus.valueOf(error.getStatusCode()))
                .body(new ErrorResponse(error.getCode(), error.getMessage()));
    }
}
