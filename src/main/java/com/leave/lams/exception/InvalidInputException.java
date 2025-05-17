package com.leave.lams.exception;

@SuppressWarnings("serial")
public class InvalidInputException extends RuntimeException {
	public InvalidInputException(String message) {
        super(message);
    }
}
