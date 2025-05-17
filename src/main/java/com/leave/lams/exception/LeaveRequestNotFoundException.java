package com.leave.lams.exception;

@SuppressWarnings("serial")
public class LeaveRequestNotFoundException extends RuntimeException {
    public LeaveRequestNotFoundException(String message) {
        super(message);
    }
}
