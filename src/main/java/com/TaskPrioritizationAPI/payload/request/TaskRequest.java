package com.TaskPrioritizationAPI.payload.request;

import java.time.LocalDate;

public record TaskRequest(String title, String description, LocalDate dueDate, boolean isCritical) {
}
