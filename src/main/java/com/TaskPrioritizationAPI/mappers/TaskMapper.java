package com.TaskPrioritizationAPI.mappers;

import com.TaskPrioritizationAPI.dtos.TaskDTO;
import com.TaskPrioritizationAPI.models.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

//    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static TaskDTO toDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setPriority(task.getPriority());
        dto.setDueDate(task.getDueDate());
        dto.setIsCompleted(task.isCompleted());
        return dto;
    }

    public static Task toEntity(TaskDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setDueDate(dto.getDueDate());
        task.setCompleted(dto.getIsCompleted());
        return task;
    }
}
