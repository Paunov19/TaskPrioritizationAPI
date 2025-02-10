package com.TaskPrioritizationAPI.services.impl;

import com.TaskPrioritizationAPI.dtos.TaskDTO;
import com.TaskPrioritizationAPI.exceptions.InvalidTaskDateException;
import com.TaskPrioritizationAPI.exceptions.TaskAlreadyExistsException;
import com.TaskPrioritizationAPI.exceptions.TaskNotExistsException;
import com.TaskPrioritizationAPI.mappers.TaskMapper;
import com.TaskPrioritizationAPI.models.Priority;
import com.TaskPrioritizationAPI.models.Task;
import com.TaskPrioritizationAPI.payload.request.TaskRequest;
import com.TaskPrioritizationAPI.repositories.TaskRepository;
import com.TaskPrioritizationAPI.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public TaskDTO createTask(TaskRequest taskRequest) {
        if (taskRequest.dueDate().isBefore(LocalDate.now())) {
            throw new InvalidTaskDateException("The due date cannot be in the past: " + taskRequest.dueDate());
        }
        if (taskRepository.findByTitleAndDueDate(taskRequest.title(), taskRequest.dueDate()).isPresent()) {
            throw new TaskAlreadyExistsException(
                    "A task with the same title and due date already exists: title:" + taskRequest.title() + " date: " + taskRequest.dueDate()
            );
        }
        if (!isValidDate(taskRequest.dueDate().toString())) {
            throw new InvalidTaskDateException("Invalid date format. Please use yyyy-MM-dd.");
        }
        if (!"true".equalsIgnoreCase(String.valueOf(taskRequest.isCritical())) && !"false".equalsIgnoreCase(String.valueOf(taskRequest.isCritical()))) {
            throw new IllegalArgumentException("Invalid value for critical status. Use true or false.");
        }
        Task task = new Task();
        task.setTitle(taskRequest.title());
        task.setDescription(taskRequest.description());
        task.setPriority(calculatePriority(taskRequest.dueDate(), taskRequest.isCritical()));
        task.setDueDate(taskRequest.dueDate());
        task.setCompleted(false);
        taskRepository.save(task);
        return TaskMapper.toDTO(task);
    }

    @Override
    public List<TaskDTO> getTasksSorted(String sort) {
        List<Task> tasks = taskRepository.findAll();
        String normalizeSort = normalize(sort);
        if (normalizeSort.isEmpty() || normalizeSort.equals("priority")) {
            tasks.sort(Comparator.comparing(Task::getPriority).thenComparing(Task::getDueDate));
        } else if (normalizeSort.equals("date")) {
            tasks.sort(Comparator.comparing(Task::getDueDate));
        } else {
            throw new IllegalArgumentException("Unsupported sort option: " + normalizeSort + "Supported sort options are 'priority' or 'date'.");
        }

        return tasks.stream().map(TaskMapper::toDTO).collect(Collectors.toList());
    }


    @Override
    public List<TaskDTO> getTasksByFilter(String filter, String value) {
        String normalizeFilter = normalize(filter);
        String normalizeValue = normalize(value);

        if (!"completed".equalsIgnoreCase(normalizeFilter) && !"priority".equalsIgnoreCase(normalizeFilter)) {
            throw new IllegalArgumentException("Unsupported filter: " + normalizeFilter + ". Supported filters are 'completed' or 'priority'.");
        }
        List<Task> tasks = switch (normalizeFilter.toLowerCase()) {
            case "completed" -> {
                if (!"true".equalsIgnoreCase(normalizeValue) && !"false".equalsIgnoreCase(normalizeValue)) {
                    throw new IllegalArgumentException("Invalid value for completed filter. Use true or false.");
                }
                boolean isCompleted = Boolean.parseBoolean(normalizeValue);
                yield taskRepository.findByIsCompleted(isCompleted);
            }
            case "priority" -> {
                if (!"HIGH".equalsIgnoreCase(normalizeValue) && !"MEDIUM".equalsIgnoreCase(normalizeValue) && !"LOW".equalsIgnoreCase(normalizeValue)) {
                    throw new IllegalArgumentException("Invalid value for priority filter. Use high, medium or low.");
                }
                Priority priority = Priority.valueOf(normalizeValue.toUpperCase());
                yield taskRepository.findByPriority(priority);
            }
            default -> throw new IllegalArgumentException("Unsupported filter: " + normalizeFilter);
        };
        return tasks.stream().map(TaskMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDto) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + id));

        if (taskDto.getTitle() != null) {
            task.setTitle(taskDto.getTitle());
        }
        if (taskDto.getDescription() != null) {
            task.setDescription(taskDto.getDescription());
        }
        if (taskDto.getDueDate() != null) {
            if (!isValidDate(taskDto.getDueDate().toString())) {
                throw new InvalidTaskDateException("Invalid date format. Please use yyyy-MM-dd.");
            } else if (taskDto.getDueDate().isBefore(LocalDate.now())) {
                throw new InvalidTaskDateException("The due date cannot be in the past: " + taskDto.getDueDate());
            }
            task.setDueDate(taskDto.getDueDate());
        }
        if (taskDto.getPriority() != null) {
            if (taskDto.getIsCompleted()) {
                task.setPriority(Priority.LOW);
            } else {
                task.setPriority(taskDto.getPriority());
            }
        }
        if (taskDto.getIsCompleted() != null) {
            if (!"true".equalsIgnoreCase(String.valueOf(taskDto.getIsCompleted())) && !"false".equalsIgnoreCase(String.valueOf(taskDto.getIsCompleted()))) {
                throw new IllegalArgumentException("Invalid value for critical status. Use true or false.");
            }
            task.setCompleted(taskDto.getIsCompleted());
            if (taskDto.getIsCompleted()) {
                task.setPriority(Priority.LOW);
            }
        }
        taskRepository.save(task);
        return TaskMapper.toDTO(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotExistsException("Task not found with id: " + id));
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotExistsException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    private Priority calculatePriority(LocalDate dueDate, boolean isCritical) {
        if (isCritical) {
            return Priority.HIGH;
        }
        if (dueDate.isBefore(LocalDate.now().plusDays(7))) {
            return Priority.HIGH;
        } else if (dueDate.isBefore(LocalDate.now().plusDays(20))) {
            return Priority.MEDIUM;
        } else {
            return Priority.LOW;
        }
    }

    private static String normalize(String input) {
        return (input == null) ? "" : input.replaceAll("\\s+", "");
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
