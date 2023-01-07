package dev.psulej.taskapp.task;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping()
    public PaginationResponse<Task> getTasks(
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size
    ) {
        return taskService.getPage(page, size, sort);
    }

    @GetMapping("/{id}")
    public Task getTask(@PathVariable long id) {
        return taskService.get(id);
    }

    @PostMapping
    public Task createTask(@RequestBody TaskRequest  newTask) {
        return taskService.create(newTask);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable long id) {
        taskService.delete(id);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable long id, @RequestBody TaskRequest  existingTask) {
        return taskService.update(id, existingTask);
    }


}
