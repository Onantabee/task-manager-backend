package com.tskmgmnt.rhine.controller;

import com.tskmgmnt.rhine.dto.TaskReq;
import com.tskmgmnt.rhine.dto.TaskResponse;
import com.tskmgmnt.rhine.entity.Task;
import com.tskmgmnt.rhine.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "*")
public class  TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(path = "/create-task")
    public String createTask (@RequestBody TaskReq request) {
        return taskService.createTask(request).getTitle();
    }
    @GetMapping()
    public List<TaskResponse> getTasks (){
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById (@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @PutMapping("update-task/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody TaskReq taskReq) {
        return taskService.updateTaskById(id, taskReq);
    }

    @PutMapping("/{id}/status")
    public TaskResponse updateTaskStatus(@PathVariable Long id, @RequestBody TaskReq taskReq) {
        return taskService.updateStatusById(id, taskReq);
    }

    @DeleteMapping("{id}")
    public Task deleteTask(@PathVariable Long id) {
        return taskService.deleteTaskById(id);
    }
}
