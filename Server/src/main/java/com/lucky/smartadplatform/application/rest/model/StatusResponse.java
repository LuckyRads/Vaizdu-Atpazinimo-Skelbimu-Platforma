package com.lucky.smartadplatform.application.rest.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Generic REST API response class that only represents operation's status.
 * 
 * @author lucky
 */
@Getter
@Setter
public class StatusResponse {

	public enum StatusCode {
		SUCCESS(0),
		PARTIAL_SUCCESS(1),
		FAILED(2);

		private final Integer value;

		private StatusCode(int value) {
			this.value = value;
		}

		public int toInt() {
			return this.value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}

	}

	protected StatusCode status;

	protected String message;

	public StatusResponse() {
		this.status = StatusCode.SUCCESS;
	}

	public StatusResponse(String message) {
		this.status = StatusCode.SUCCESS;
		this.message = message;
	}

	public StatusResponse(StatusCode status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

}
