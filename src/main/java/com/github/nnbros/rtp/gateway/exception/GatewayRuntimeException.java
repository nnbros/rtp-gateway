package com.github.nnbros.rtp.gateway.exception;

import com.github.nnbros.rtp.common.exception.RtpRuntimeException;

public class GatewayRuntimeException extends RtpRuntimeException {

	public GatewayRuntimeException() {
	}

	public GatewayRuntimeException(String message, Object... params) {
		super(message, params);
	}

	public GatewayRuntimeException(String message, Throwable cause, Object... params) {
		super(message, cause, params);
	}

	public GatewayRuntimeException(Throwable cause) {
		super(cause);
	}
}
