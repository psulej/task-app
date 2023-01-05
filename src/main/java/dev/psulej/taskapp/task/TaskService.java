package dev.psulej.taskapp.task;

import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TaskValidator taskValidator;
    private final TaskRepository taskRepository;


    public TaskService(TaskValidator taskValidator, TaskRepository taskRepository) {
        this.taskValidator = taskValidator;
        this.taskRepository = taskRepository;
    }

    public PaginationResponse<Task> getPage(int page, int size, String sort) {
        return taskRepository.getPage(page, size, sort);
    }

    public Task get(long id) {
        return taskRepository.get(id);
    }

    public Task create(Task newTask) {
        taskValidator.validate(newTask);
        return taskRepository.create(newTask);
    }

    public void delete(long id) {
        taskRepository.delete(id);
    }

    public Task update(long id, Task existingTask) {
        taskValidator.validate(existingTask);
        return taskRepository.update(id, existingTask);
    }
}