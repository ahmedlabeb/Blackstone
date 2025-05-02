package com.blackstone.customer.exception.handler;

import com.blackstone.customer.exception.*;
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

    @ExceptionHandler(CustomerServiceException.class)
    public final ResponseEntity<CustomerServiceErrorResponse> handleException(final RestException exception) {
        final RestError restError = exception.getRestError();
        final CustomerServiceErrorResponse errorResponse = CustomerServiceErrorResponse.builder().errorMessage(restError.desceription()).error(restError.error())
                .status(restError.httpStatus().name()).build();
        return new ResponseEntity<>(errorResponse, restError.httpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomerServiceErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        final CustomerServiceErrorResponse errorResponse = CustomerServiceErrorResponse.builder().errorMessage(CustomerServiceError.REQUEST_VALIDATION_ERROR.desceription()).error(CustomerServiceError.REQUEST_VALIDATION_ERROR.error())
                .status(CustomerServiceError.REQUEST_VALIDATION_ERROR.httpStatus().toString()).build();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errorResponse, CustomerServiceError.REQUEST_VALIDATION_ERROR.httpStatus());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Internal server error: " + ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

