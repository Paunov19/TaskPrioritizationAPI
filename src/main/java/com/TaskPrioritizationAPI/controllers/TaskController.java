package com.TaskPrioritizationAPI.controllers;

import com.TaskPrioritizationAPI.dtos.TaskDTO;
import com.TaskPrioritizationAPI.exceptions.TaskNotExistsException;
import com.TaskPrioritizationAPI.models.Task;
import com.TaskPrioritizationAPI.payload.request.TaskRequest;
import com.TaskPrioritizationAPI.services.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Create a new task",
            description = "This endpoint allows user to create a new task.")
    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskRequest taskRequest) {
        TaskDTO taskDTO = taskService.createTask(taskRequest);
        return new ResponseEntity<>(taskDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Get sorted tasks",
            description = "Retrieve all tasks sorted by priority or date.")
    @GetMapping("/sort")
    public ResponseEntity<List<TaskDTO>> sortTasks(@RequestParam(required = false) String sort) {
        List<TaskDTO> tasks = taskService.getTasksSorted(sort);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Filter tasks",
            description = "Filter tasks by completion status or priority (e.g., completed=true or priority=HIGH).")
    @GetMapping("/filter")
    public ResponseEntity<List<TaskDTO>> filterTasks(
            @RequestParam(value = "filter") String filter,
            @RequestParam(value = "value") String value) {

        List<TaskDTO> tasks = taskService.getTasksByFilter(filter, value);
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Update an existing task",
            description = "Update the details of a specific task by its ID.")
    @PatchMapping("/update/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @RequestBody TaskDTO taskDTO) {
        TaskDTO taskDto = taskService.updateTask(id, taskDTO);
        return new ResponseEntity<>(taskDto, HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Delete task",
            description = "Delete a task by its ID.")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        try {
            taskService.deleteTask(id);
            return ResponseEntity.ok("Task deleted successfully");
        } catch (TaskNotExistsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(summary = "Get all tasks",
            description = "Retrieve all tasks with all details.")
    @GetMapping("/get-all-tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @Operation(summary = "Get task by id",
            description = "Retrieve а task by id.")
    @GetMapping("/get-task/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task tasks = taskService.getTaskById(id);
        return ResponseEntity.ok(tasks);
    }
}