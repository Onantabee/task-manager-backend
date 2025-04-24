package com.tskmgmnt.rhine.controller;

import com.tskmgmnt.rhine.dto.TaskDto;
import com.tskmgmnt.rhine.dto.TaskDto;
import com.tskmgmnt.rhine.entity.Task;
import com.tskmgmnt.rhine.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "*")
@Tag(
        name = "CRUD REST APIs for Task Management",
        description = "CRUD REST APIs - Create Task, Update Task, Get Task, Get All Tasks, Delete Task, Update Task Status"
)
public class TaskController {

    private final TaskService taskService;
    private final SimpMessagingTemplate messagingTemplate;

    public TaskController(TaskService taskService, SimpMessagingTemplate messagingTemplate) {
        this.taskService = taskService;
        this.messagingTemplate = messagingTemplate;
    }

    @Operation(
            summary = "Create a new task",
            description = "Creates a new task with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping(path = "/create-task")
    public TaskDto createTask(@RequestBody TaskDto request) {
        return taskService.createTask(request);
    }

    @Operation(
            summary = "Get all tasks",
            description = "Retrieves a list of all tasks in the system",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved tasks"),
                    @ApiResponse(responseCode = "204", description = "No tasks found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping()
    public List<TaskDto> getTasks() {
        return taskService.getAllTasks();
    }

    @Operation(
            summary = "Get task by ID",
            description = "Retrieves a specific task by its unique identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved task"),
                    @ApiResponse(responseCode = "404", description = "Task not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/{id}")
    public TaskDto getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @Operation(
            summary = "Update task details",
            description = "Updates all details of an existing task",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Task not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PutMapping("update-task/{id}")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskDto taskReq) {
        return taskService.updateTaskById(id, taskReq);
    }

    @Operation(
            summary = "Update task status",
            description = "Updates only the status of an existing task",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task status updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Task not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PutMapping("/{id}/status")
    public TaskDto updateTaskStatus(@PathVariable Long id, @RequestBody TaskDto taskReq) {
        return taskService.updateStatusById(id, taskReq);
    }

    @Operation(
            summary = "Update task 'is new' state",
            description = "Updates the 'is new' flag of a task (typically used to mark a task as read/seen)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task state updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "404", description = "Task not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PutMapping("task-is-new-state/{id}")
    public TaskDto updateTaskNewState(@PathVariable Long id, @RequestBody TaskDto taskReq) {
        return taskService.updateIsNewState(id, taskReq);
    }

    @Operation(
            summary = "Get task 'is new' state",
            description = "Retrieves the current 'is new' state of a task (indicating if the task is new/unseen)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved task state"),
                    @ApiResponse(responseCode = "404", description = "Task not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/{id}/is-new")
    public TaskDto getIsNewState(@PathVariable Long id) {
        return taskService.getIsNewState(id);
    }

    @Operation(
            summary = "Delete a task",
            description = "Deletes a specific task by its unique identifier",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Task deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Task not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @DeleteMapping("{id}")
    public Task deleteTask(@PathVariable Long id) {
        return taskService.deleteTaskById(id);
    }
}