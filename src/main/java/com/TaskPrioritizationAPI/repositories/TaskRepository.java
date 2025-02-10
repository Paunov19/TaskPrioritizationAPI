package com.TaskPrioritizationAPI.repositories;

import com.TaskPrioritizationAPI.models.Priority;
import com.TaskPrioritizationAPI.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByTitleAndDueDate(String title, LocalDate dueDate);
    List<Task> findByIsCompleted(boolean completed);
    List<Task> findByPriority(Priority priority);
}
