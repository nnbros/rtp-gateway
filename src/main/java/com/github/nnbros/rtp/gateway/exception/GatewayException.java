package com.github.nnbros.rtp.gateway.exception;

public class GatewayException extends Exception {

	public GatewayException() {
	}

	public GatewayException(String message) {
		super(message);
	}

	public GatewayException(String message, Throwable cause) {
		super(message, cause);
	}

	public GatewayException(Throwable cause) {
		super(cause);
	}
}
