package com.tskmgmnt.rhine.controller;

import com.tskmgmnt.rhine.dto.TaskDto;
import com.tskmgmnt.rhine.dto.TaskDto;
import com.tskmgmnt.rhine.entity.Task;
import com.tskmgmnt.rhine.service.TaskService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "*")
public class  TaskController {

    private final TaskService taskService;
    private final SimpMessagingTemplate messagingTemplate;

    public TaskController(TaskService taskService, SimpMessagingTemplate messagingTemplate) {
        this.taskService = taskService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping(path = "/create-task")
    public TaskDto createTask (@RequestBody TaskDto request) {
        return taskService.createTask(request);
    }
    @GetMapping()
    public List<TaskDto> getTasks (){
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskDto getTaskById (@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @PutMapping("update-task/{id}")
    public TaskDto updateTask(@PathVariable Long id, @RequestBody TaskDto taskReq) {
        return taskService.updateTaskById(id, taskReq);
    }

    @PutMapping("/{id}/status")
    public TaskDto updateTaskStatus(@PathVariable Long id, @RequestBody TaskDto taskReq) {
        return taskService.updateStatusById(id, taskReq);
    }

    @DeleteMapping("{id}")
    public Task deleteTask(@PathVariable Long id) {
        return taskService.deleteTaskById(id);
    }
}