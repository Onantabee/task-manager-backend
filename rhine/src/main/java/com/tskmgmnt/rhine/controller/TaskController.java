package com.tskmgmnt.rhine.controller;

import com.tskmgmnt.rhine.dto.TaskReq;
import com.tskmgmnt.rhine.entities.Task;
import com.tskmgmnt.rhine.services.TaskService;
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
    public List<Task> getTasks (){
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public Task getTaskById (@PathVariable Long id){
        return taskService.getTaskById(id);
    }

    @DeleteMapping("/{id}")
    public Task deleteTask(@PathVariable Long id) {
        return taskService.deleteTaskById(id);
    }
}
