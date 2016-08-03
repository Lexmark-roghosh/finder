package com.finder.helper;

public class ConnectionUnavailableException extends RuntimeException {

	public ConnectionUnavailableException() {
		super();
	}

	public ConnectionUnavailableException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ConnectionUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConnectionUnavailableException(String message) {
		super(message);
	}

	public ConnectionUnavailableException(Throwable cause) {
		super(cause);
	}
	
	

}
