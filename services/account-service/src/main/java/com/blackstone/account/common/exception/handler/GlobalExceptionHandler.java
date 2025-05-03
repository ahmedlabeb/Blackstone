package com.blackstone.account.common.exception.handler;

import com.blackstone.account.common.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccountServiceException.class)
    public final ResponseEntity<AccountServiceErrorResponse> handleException(final RestException exception) {
        final RestError restError = exception.getRestError();
        final AccountServiceErrorResponse errorResponse = AccountServiceErrorResponse.builder().errorMessage(restError.desceription()).error(restError.error())
                .status(restError.httpStatus().name()).build();
        return new ResponseEntity<>(errorResponse, restError.httpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AccountServiceErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        final AccountServiceErrorResponse errorResponse = AccountServiceErrorResponse.builder().errorMessage(AccountServiceError.REQUEST_VALIDATION_ERROR.desceription()).error(AccountServiceError.REQUEST_VALIDATION_ERROR.error())
                .status(AccountServiceError.REQUEST_VALIDATION_ERROR.httpStatus().toString()).build();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errorResponse, AccountServiceError.REQUEST_VALIDATION_ERROR.httpStatus());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal server error: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

