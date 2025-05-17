package com.leave.lams.exception;

@SuppressWarnings("serial")
public class ShiftNotFoundException extends RuntimeException{
	public ShiftNotFoundException(String message) {
		super(message);
	}

}
