package com.TaskPrioritizationAPI.exceptions;

public class InvalidTaskDateException extends RuntimeException {
    public InvalidTaskDateException(String message) {
        super(message);
    }
}