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

//    @GetMapping
//    List<Task> getTasks() {
//        return taskService.getTasks();
//    }

    @GetMapping()
    public PaginationResponse<Task> getTasks(
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ){
        return taskService.getTasks(page,size,sort);
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable long id) {
        return taskService.getTask(id);
    }

    @PostMapping
    public Task createTask(@RequestBody Task newTask) {
        return taskService.createTask(newTask);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) {
        taskService.deleteTask(id);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable long id, @RequestBody Task existingTask) {
        return taskService.updateTask(id,existingTask);
    }


}
