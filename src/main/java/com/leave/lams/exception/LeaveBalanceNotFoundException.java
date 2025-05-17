package com.leave.lams.exception;

@SuppressWarnings("serial")
public class LeaveBalanceNotFoundException extends RuntimeException {
    public LeaveBalanceNotFoundException(String message) {
        super(message);
    }
}
