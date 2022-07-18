package com.hrcentral.nphc.helper;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NO_CONTENT)
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = -8282205145532263081L;
	
	public CustomException(String message) {
		super(message);
		System.out.println("message: "+message);
	}
	public CustomException(String message, Throwable t) {
		super(message,t);
	}
	public CustomException(Throwable t) {
		super(t);
	}

}
