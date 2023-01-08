package dev.psulej.taskapp.task;

import dev.psulej.taskapp.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final UserService userService;
    private final TaskRepository taskRepository;


    public TaskService(UserService userService, TaskRepository taskRepository) {
        this.userService = userService;
        this.taskRepository = taskRepository;
    }

    public PaginationResponse<Task> getPage(int page, int size, String sort) {
        long userId = userService.getLoggedUserId();
        return taskRepository.getPage(userId, page, size, sort);
    }

    public Task get(long id) {
        return taskRepository.get(id);
    }

    public Task create(TaskRequest newTask) {
        long userId = userService.getLoggedUserId();
        return taskRepository.create(userId,newTask);
    }

    public void delete(long id) {
        taskRepository.delete(id);
    }

    public Task update(long id, TaskRequest  existingTask) {
        long userId = userService.getLoggedUserId();
        return taskRepository.update(id, userId, existingTask);
    }
}