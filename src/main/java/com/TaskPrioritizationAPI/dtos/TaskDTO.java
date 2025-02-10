package com.TaskPrioritizationAPI.dtos;

import com.TaskPrioritizationAPI.models.Priority;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private String title;
    private String description;
    private Priority priority;
    private LocalDate dueDate;
    private Boolean isCompleted;
}