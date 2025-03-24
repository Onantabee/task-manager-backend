package com.tskmgmnt.rhine.service;

import com.tskmgmnt.rhine.dto.TaskReq;
import com.tskmgmnt.rhine.dto.TaskResponse;
import com.tskmgmnt.rhine.entity.Task;
import com.tskmgmnt.rhine.entity.User;
import com.tskmgmnt.rhine.repository.TaskRepository;
import com.tskmgmnt.rhine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<TaskResponse> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
    }

    private TaskResponse mapToTaskResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setDueDate(task.getDueDate());
        response.setPriority(task.getPriority());
        response.setTaskStatus(task.getTaskStatus());
        response.setCreatedById(task.getCreatedBy().getEmail()); // Assuming email is the ID
        if (task.getAssignee() != null) {
            response.setAssigneeId(task.getAssignee().getEmail()); // Assuming email is the ID
        }
        return response;
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not Found!"));
        return mapToTaskResponse(task);
    }

    public Task updateTaskById(Long id, TaskReq taskReq) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not Found!"));

        existingTask.setTitle(taskReq.getTitle());
        existingTask.setDescription(taskReq.getDescription());
        existingTask.setTaskStatus(taskReq.getTaskStatus());
        existingTask.setPriority(taskReq.getPriority());
        existingTask.setDueDate(taskReq.getDueDate());
        if (taskReq.getCreatedById() != null) {
            User createdBy = userRepository.findByEmail(taskReq.getCreatedById())
                    .orElseThrow(() -> new RuntimeException("User not Found!"));
            existingTask.setCreatedBy(createdBy);
        }
        if (taskReq.getAssigneeId() != null) {
            User assignee = userRepository.findByEmail(taskReq.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not Found!"));
            existingTask.setAssignee(assignee);
        } else {
            existingTask.setAssignee(null);
        }

        return taskRepository.save(existingTask);
    }

    public TaskResponse updateStatusById(Long id, TaskReq taskReq) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not Found!"));

        task.setTaskStatus(taskReq.getTaskStatus());
        Task updatedTask = taskRepository.save(task);

        return mapToTaskResponse(updatedTask);
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