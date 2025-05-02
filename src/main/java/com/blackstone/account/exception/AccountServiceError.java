package com.blackstone.account.exception;

import org.springframework.http.HttpStatus;

public enum AccountServiceError implements RestError {


    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UnAuthorized"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),

    REQUEST_VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Account Invalid Request"),

    SERVICE_UN_AVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Account Service is unavailable"),

    ACCOUNT_ALREADY_EXIST(HttpStatus.CONFLICT, "Account Already exist"),
    ACCOUNT_NOT_EXIST(HttpStatus.NOT_FOUND, "No Account exist with this Id");

    AccountServiceError(final HttpStatus httpStatus, final String description) {
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
        return new AccountServiceException(this);
    }

    public RestException buildException(String message) {
        return new AccountServiceException(this, message);
    }
}
