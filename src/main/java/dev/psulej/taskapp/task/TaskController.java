package dev.psulej.taskapp.task;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    List<Task> getTasks(){
        return taskService.getTasks();
    }

    @PostMapping
    public Task createTask(@RequestBody Task newTask) {
        return taskService.createTask(newTask);
    }


}
