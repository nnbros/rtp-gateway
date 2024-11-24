package com.github.nnbros.rtp.gateway.exception;

import lombok.Getter;

@Getter
public class UpdateRuntimeException extends RuntimeException {
	private Long userId;

	public UpdateRuntimeException() {
	}

	public UpdateRuntimeException(String message) {
		super(message);
	}

	public UpdateRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public UpdateRuntimeException(Throwable cause) {
		super(cause);
	}

	public UpdateRuntimeException(String message, Long userId) {
		super(message);
		this.userId = userId;
	}
}
