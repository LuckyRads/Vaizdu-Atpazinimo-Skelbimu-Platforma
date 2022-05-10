package com.lucky.smartadplatform.application.rest.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Generic REST API response class, which contains operation status, message and
 * passed data.
 * 
 * @author lucky
 *
 * @param <T> data to be returned
 */
@Getter
@Setter
public class GenericResponse<T> extends StatusResponse {

	private T data;

	public GenericResponse() {
		this.status = StatusCode.SUCCESS;
	}

	public GenericResponse(T data) {
		this.status = StatusCode.SUCCESS;
		this.data = data;
	}

	public GenericResponse(StatusCode status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public GenericResponse(StatusCode status, String message, T data) {
		super();
		this.status = status;
		this.message = message;
		this.data = data;
	}

}
