package com.tskmgmnt.rhine.service;

import com.tskmgmnt.rhine.dto.NotificationDto;
import com.tskmgmnt.rhine.dto.TaskDto;
import com.tskmgmnt.rhine.dto.TaskDto;
import com.tskmgmnt.rhine.entity.Task;
import com.tskmgmnt.rhine.entity.User;
import com.tskmgmnt.rhine.repository.TaskRepository;
import com.tskmgmnt.rhine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public TaskDto createTask(TaskDto taskReq) {
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

        Task savedTask = taskRepository.save(task);

        messagingTemplate.convertAndSend("/topic/task-created",
                new NotificationDto("TASK_CREATED", mapToTaskResponse(savedTask)));

        return mapToTaskResponse(savedTask);
    }

    public List<TaskDto> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(this::mapToTaskResponse)
                .collect(Collectors.toList());
    }

    private TaskDto mapToTaskResponse(Task task) {
        TaskDto response = new TaskDto();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setDueDate(task.getDueDate());
        response.setPriority(task.getPriority());
        response.setTaskStatus(task.getTaskStatus());
        response.setCreatedById(task.getCreatedBy().getEmail());
        if (task.getAssignee() != null) {
            response.setAssigneeId(task.getAssignee().getEmail());
        }
        return response;
    }

    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not Found!"));
        return mapToTaskResponse(task);
    }

    public TaskDto updateTaskById(Long id, TaskDto taskReq) {
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
        Task updatedTask = taskRepository.save(existingTask);
        TaskDto taskResponse = mapToTaskResponse(updatedTask);
        messagingTemplate.convertAndSend("/topic/task-updated",
                new NotificationDto("TASK_UPDATED", taskResponse));
        return taskResponse;
    }

    public TaskDto updateStatusById(Long id, TaskDto taskReq) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not Found!"));

        task.setTaskStatus(taskReq.getTaskStatus());
        Task updatedTask = taskRepository.save(task);

        messagingTemplate.convertAndSend("/topic/task-status-updated",
                new NotificationDto("TASK_STATUS_UPDATED", mapToTaskResponse(updatedTask)));

        return mapToTaskResponse(updatedTask);
    }

    public Task deleteTaskById(Long id) {
        try {
            Optional<Task> taskToDelete = taskRepository.findById(id);
            if (taskToDelete.isPresent()) {
                Task task = taskToDelete.get();
                TaskDto response = mapToTaskResponse(task);
                taskRepository.delete(task);
                System.out.println("Task deleted successfully: " + task.getId());
                messagingTemplate.convertAndSend("/topic/task-deleted",
                        new NotificationDto("TASK_DELETED", response.getId()));
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