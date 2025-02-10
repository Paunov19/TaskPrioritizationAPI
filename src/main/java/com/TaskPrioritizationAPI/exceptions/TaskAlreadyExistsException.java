package com.TaskPrioritizationAPI.exceptions;

public class TaskAlreadyExistsException extends RuntimeException{
    public TaskAlreadyExistsException(String message) {
        super(message);
    }
}
