package com.blackstone.account.exception;

import lombok.*;

@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AccountServiceErrorResponse {
	 /** The error message . */
    private String errorMessage;

    /** The error Code . */
    private String error;

    /** The status. */
    private String status;
    /**
     * Request time Stamp
     */
    private Long timestamp;
}
