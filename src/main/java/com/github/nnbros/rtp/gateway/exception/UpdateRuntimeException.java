package com.github.nnbros.rtp.gateway.exception;

import lombok.Getter;

@Getter
public class UpdateRuntimeException extends RuntimeException {
	private Long userId;
	private String callbackQueryId;

	public UpdateRuntimeException(String message) {
		super(message);
	}

	public UpdateRuntimeException(String message, Long userId) {
		super(message);
		this.userId = userId;
	}

	public UpdateRuntimeException(String message, String callbackQueryId, Long userId) {
		super(message);
		this.callbackQueryId = callbackQueryId;
		this.userId = userId;
	}
}
