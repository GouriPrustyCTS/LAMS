package com.leave.lams.exception;

@SuppressWarnings("serial")
public class AttendanceNotFoundException extends RuntimeException {
    public AttendanceNotFoundException(String message) {
        super(message);
    }
}

