
package com.blackstone.customer.exception;

import org.springframework.http.HttpStatus;

public interface RestError {

	String error();

	HttpStatus httpStatus();

	String desceription();

	void setDescription(String message);
}
