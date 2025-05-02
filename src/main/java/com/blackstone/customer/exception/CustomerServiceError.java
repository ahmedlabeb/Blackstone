package com.blackstone.customer.exception;

import org.springframework.http.HttpStatus;

public enum CustomerServiceError implements RestError {


    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UnAuthorized"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),

    REQUEST_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Customer Invalid Request"),

    SERVICE_UN_AVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Customer Service is unavailable"),

    CUSTOMER_ALREADY_EXIST(HttpStatus.CONFLICT, "Customer Already exist"),
    CUSTOMER_NOT_EXIST(HttpStatus.NOT_FOUND, "No Customer exist with this Id");

    CustomerServiceError(final HttpStatus httpStatus, final String description) {
        this.httpStatus = httpStatus;
        this.description = description;
    }

    /**
     * The http status.
     */
    private HttpStatus httpStatus;

    /**
     * The description.
     */
    private String description;

    @Override
    public String error() {
        return this.name();
    }

    @Override
    public HttpStatus httpStatus() {
        return this.httpStatus;
    }

    @Override
    public String desceription() {
        return this.description;
    }

    public void setDescription(String message) {
        this.description = message;
    }

    public RestException buildException() {
        return new CustomerServiceException(this);
    }

    public RestException buildException(String message) {
        return new CustomerServiceException(this, message);
    }
}
