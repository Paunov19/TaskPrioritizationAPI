package com.TaskPrioritizationAPI.bootstrap;

import com.TaskPrioritizationAPI.models.Priority;
import com.TaskPrioritizationAPI.models.Task;
import com.TaskPrioritizationAPI.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataBaseSeeder implements CommandLineRunner {

        @Autowired
        private TaskRepository taskRepository;

        @Override
        public void run(String... args) {
            if (taskRepository.count() == 0) {
                seedTasks();
            }
        }

        private void seedTasks() {
            List<Task> tasks = new ArrayList<>();
            Task task1 = new Task();
            task1.setTitle("Test Task 1");
            task1.setDescription("Description for Test task 1");
            task1.setPriority(Priority.HIGH);
            task1.setDueDate(LocalDate.of(2025,2,1));
            task1.setCompleted(true);
            tasks.add(task1);

            Task task2 = new Task();
            task2.setTitle("Test Task 2");
            task2.setDescription("Description for Test task 2");
            task2.setPriority(Priority.MEDIUM);
            task2.setDueDate(LocalDate.of(2025,3,1));
            task2.setCompleted(false);
            tasks.add(task2);

            Task task3 = new Task();
            task3.setTitle("Test Task 3");
            task3.setDescription("Description for Test task 3");
            task3.setPriority(Priority.LOW);
            task3.setDueDate(LocalDate.of(2025,4,1));
            task3.setCompleted(false);
            tasks.add(task3);

            taskRepository.saveAll(tasks);

            System.out.println("Database seeded with initial data.");
        }
    }
