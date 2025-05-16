package com.leave.lams.exception;

public class LeaveRequestNotFoundException extends RuntimeException {
    public LeaveRequestNotFoundException(String message) {
        super(message);
    }
}
