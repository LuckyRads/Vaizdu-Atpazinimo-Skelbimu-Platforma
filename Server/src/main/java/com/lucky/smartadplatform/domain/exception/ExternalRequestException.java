package com.lucky.smartadplatform.domain.exception;

/**
 * This exception is thrown when external requests fail.
 * 
 * @author lucky
 */
public class ExternalRequestException extends RuntimeException {

	private static final long serialVersionUID = 890934103299106836L;

	public ExternalRequestException() {
		super();
	}

	public ExternalRequestException(String message) {
		super(message);
	}

}
