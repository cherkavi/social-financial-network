package com.cherkashyn.vitalii.components.sms.remoteservice.transport;

/**
 * exception during execute http connection
 */
public class TransportException extends Exception{

	private static final long serialVersionUID = 1L;

	public TransportException(String message, Throwable cause) {
		super(message, cause);
	}

	public TransportException(String message) {
		super(message);
	}

	public TransportException(Exception e) {
		super(e);
	}
	
	
}
