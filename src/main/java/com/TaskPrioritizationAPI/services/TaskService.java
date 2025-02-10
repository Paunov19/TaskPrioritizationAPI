package com.TaskPrioritizationAPI.services;

import com.TaskPrioritizationAPI.dtos.TaskDTO;
import com.TaskPrioritizationAPI.models.Task;
import com.TaskPrioritizationAPI.payload.request.TaskRequest;

import java.util.List;

public interface TaskService {
    TaskDTO createTask(TaskRequest taskRequest);
    List<TaskDTO> getTasksSorted(String sort);
    List<TaskDTO> getTasksByFilter(String filter, String value);
    TaskDTO updateTask(Long id, TaskDTO taskDTO);
    void deleteTask(Long id);
    Task getTaskById(Long id);
    List<Task> getAllTasks();
}
