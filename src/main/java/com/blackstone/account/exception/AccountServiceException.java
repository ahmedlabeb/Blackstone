package com.blackstone.account.exception;

import lombok.Getter;

@Getter
public class AccountServiceException extends RestException {
    private static final long serialVersionUID = -3102904573968573206L;
    private AccountServiceError accountServiceError;

    public AccountServiceException(final AccountServiceError accountServiceError) {
        super(accountServiceError);
        this.accountServiceError = accountServiceError;
    }

    public AccountServiceException(final AccountServiceError accountServiceError, String message) {
        super(accountServiceError, message);
        this.accountServiceError = accountServiceError;
    }
}
