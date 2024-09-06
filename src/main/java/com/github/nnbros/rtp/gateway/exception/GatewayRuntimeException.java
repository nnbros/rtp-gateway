package com.github.nnbros.rtp.gateway.exception;

public class GatewayRuntimeException extends RuntimeException {

	public GatewayRuntimeException() {
	}

	public GatewayRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public GatewayRuntimeException(String message) {
		super(message);
	}

	public GatewayRuntimeException(Throwable cause) {
		super(cause);
	}
}
