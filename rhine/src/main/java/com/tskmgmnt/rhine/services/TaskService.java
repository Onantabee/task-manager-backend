package com.tskmgmnt.rhine.services;

import com.tskmgmnt.rhine.dto.TaskReq;
import com.tskmgmnt.rhine.entities.Task;
import com.tskmgmnt.rhine.entities.User;
import com.tskmgmnt.rhine.repositories.TaskRepository;
import com.tskmgmnt.rhine.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Task createTask(TaskReq taskReq) {
        Task task = new Task();
        task.setTitle(taskReq.getTitle());
        task.setDescription(taskReq.getDescription());
        task.setTaskStatus(taskReq.getTaskStatus());
        task.setPriority(taskReq.getPriority());
        task.setDueDate(taskReq.getDueDate());

        User createdBy = userRepository.findByEmail(taskReq.getCreatedById())
                .orElseThrow(() -> new RuntimeException("User not found"));
        task.setCreatedBy(createdBy);

        if (taskReq.getAssigneeId() != null) {
            User assignee = userRepository.findByEmail(taskReq.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            task.setAssignee(assignee);
        }

        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task Don't Exist!!"));
    }

    public Task deleteTaskById(Long id) {
        try {
            Optional<Task> taskToDelete = taskRepository.findById(id);
            if (taskToDelete.isPresent()) {
                Task task = taskToDelete.get();
                taskRepository.delete(task);
                taskRepository.flush();
                System.out.println("Task deleted successfully: " + task.getId());
                return task;
            } else {
                throw new RuntimeException("Task not found");
            }
        } catch (Exception e) {
            System.err.println("Error deleting task: " + e.getMessage());
            throw e;
        }
    }
}