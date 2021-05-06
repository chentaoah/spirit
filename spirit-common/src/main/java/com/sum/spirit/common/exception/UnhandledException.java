package com.sum.spirit.common.exception;

@SuppressWarnings("serial")
public class UnhandledException extends RuntimeException {

	public UnhandledException() {
		super();
	}

	public UnhandledException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnhandledException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnhandledException(String message) {
		super(message);
	}

	public UnhandledException(Throwable cause) {
		super(cause);
	}

}
