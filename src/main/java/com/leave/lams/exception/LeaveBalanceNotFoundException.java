package com.leave.lams.exception;


public class LeaveBalanceNotFoundException extends RuntimeException {
    public LeaveBalanceNotFoundException(String message) {
        super(message);
    }
}
