package com.leave.lams.exception;

@SuppressWarnings("serial")
public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException(String message) {
        super(message);
    }
}
