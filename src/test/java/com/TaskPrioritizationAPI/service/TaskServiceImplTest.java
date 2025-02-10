package com.TaskPrioritizationAPI.service;

import com.TaskPrioritizationAPI.dtos.TaskDTO;
import com.TaskPrioritizationAPI.exceptions.InvalidTaskDateException;
import com.TaskPrioritizationAPI.models.Priority;
import com.TaskPrioritizationAPI.models.Task;
import com.TaskPrioritizationAPI.payload.request.TaskRequest;
import com.TaskPrioritizationAPI.repositories.TaskRepository;
import com.TaskPrioritizationAPI.services.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTaskValidRequest() {
        TaskRequest taskRequest = new TaskRequest("Test Task", "Description", LocalDate.now().plusDays(8), false);
        Task task = new Task(null, "Test Task", "Description", Priority.MEDIUM, LocalDate.now().plusDays(5), false);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.createTask(taskRequest);

        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals(Priority.MEDIUM, result.getPriority());
    }

    @Test
    void testCreateTaskShouldThrowException() {
        TaskRequest taskRequest = new TaskRequest("Task in the Past", "Description", LocalDate.now().minusDays(1), false);

        assertThrows(InvalidTaskDateException.class, () -> taskService.createTask(taskRequest));
    }

    @Test
    void testGetTasksSortedByDate() {
        Task task1 = new Task(null, "Task 1", "Description", Priority.MEDIUM, LocalDate.now().plusDays(9), false);
        Task task2 = new Task(null, "Task 2", "Description", Priority.HIGH, LocalDate.now().plusDays(3), false);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        when(taskRepository.findAll()).thenReturn(tasks);

        List<TaskDTO> sortedTasks = taskService.getTasksSorted("date");

        assertEquals(2, sortedTasks.size());
        assertEquals("Task 2", sortedTasks.get(0).getTitle());
    }

    @Test
    void testGetTasksByFilterByCompleted() {
        Task task1 = new Task(null, "Task 1", "Description", Priority.MEDIUM, LocalDate.now().plusDays(5), true);
        when(taskRepository.findByIsCompleted(true)).thenReturn(List.of(task1));

        List<TaskDTO> completedTasks = taskService.getTasksByFilter("completed", "true");

        assertEquals(1, completedTasks.size());
        assertEquals("Task 1", completedTasks.get(0).getTitle());
    }

    @Test
    void testUpdateTaskValidRequest() {
        Task existingTask = new Task(null, "Old Title", "Old Description", Priority.LOW, LocalDate.now().plusDays(10), false);
        existingTask.setId(1L);
        TaskDTO updateRequest = new TaskDTO("New Title", "New Description", Priority.HIGH, LocalDate.now().plusDays(15), false);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        TaskDTO updatedTask = taskService.updateTask(1L, updateRequest);

        assertEquals("New Title", updatedTask.getTitle());
        assertEquals(Priority.HIGH, updatedTask.getPriority());
    }

    @Test
    void testDeleteTaskWhenTaskExists() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetTaskByIdWhenTaskExists() {
        Task task = new Task(null, "Task to Find", "Description", Priority.MEDIUM, LocalDate.now().plusDays(5), false);
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Task to Find", result.getTitle());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllTasks() {
        Task task1 = new Task(null, "Task 1", "Description", Priority.MEDIUM, LocalDate.now().plusDays(5), false);
        Task task2 = new Task(null, "Task 2", "Description", Priority.HIGH, LocalDate.now().plusDays(2), false);
        when(taskRepository.findAll()).thenReturn(List.of(task1, task2));

        List<Task> tasks = taskService.getAllTasks();

        assertEquals(2, tasks.size());
        verify(taskRepository, times(1)).findAll();
    }
}

