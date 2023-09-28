package com.example.immoapp;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Stack;

public class SQLLogException extends Exception {
    private Date timestamp;
    private String errorType;
    private String errorMessage;
    private Stack<StackTraceElement> stackTraceElements;

    public SQLLogException(SQLException sqlException) {
        this.timestamp = new Date();
        this.errorType = "SQLException";
        this.errorMessage = sqlException.getMessage();
        this.stackTraceElements = new Stack<>();
        for (StackTraceElement element : sqlException.getStackTrace()) {
            stackTraceElements.push(element);
        }
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<StackTraceElement> getStackTraceElements() {
        return stackTraceElements;
    }
}