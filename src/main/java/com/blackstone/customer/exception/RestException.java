
package com.blackstone.customer.exception;

import lombok.Getter;

@Getter
public class RestException extends RuntimeException {


    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * The Service Error errors.
     */
    private RestError restError;

    /**
     * Instantiates a new Customer Service exception.
     *
     * @param restError
     */
    public RestException(final RestError restError) {
        super(restError.desceription());
        this.restError = restError;
    }

    public RestException(final RestError restError,String message) {
        super(message);
        this.restError = restError;
    }
}
