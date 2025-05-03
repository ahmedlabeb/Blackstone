package com.blackstone.customer.common.exception;

import lombok.Getter;

@Getter
public class CustomerServiceException extends RestException {
    private static final long serialVersionUID = -3102904573968573206L;
    private CustomerServiceError customerServiceError;

    public CustomerServiceException(final CustomerServiceError customerServiceError) {
        super(customerServiceError);
        this.customerServiceError = customerServiceError;
    }

    public CustomerServiceException(final CustomerServiceError customerServiceError, String message) {
        super(customerServiceError, message);
        this.customerServiceError = customerServiceError;
    }
}
