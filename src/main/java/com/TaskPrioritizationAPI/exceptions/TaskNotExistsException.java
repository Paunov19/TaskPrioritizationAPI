package com.TaskPrioritizationAPI.exceptions;

public class TaskNotExistsException extends RuntimeException{
    public TaskNotExistsException(String message) {
        super(message);
    }
}
