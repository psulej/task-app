package dev.psulej.taskapp.task;

import org.springframework.stereotype.Service;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public PaginationResponse<Task> getPage(int page, int size, String sort) {
        return taskRepository.getPage(page, size, sort);
    }

    public Task get(long id) {
        return taskRepository.get(id);
    }

    public Task create(Task newTask) {
        if (newTask.title.length() == 0) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (newTask.content.length() == 0) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        if (newTask.dateTime == null) {
            throw new IllegalArgumentException("Date must be selected");
        }

        return taskRepository.create(newTask);
    }

    public void delete(long id) {
        taskRepository.delete(id);
    }

    public Task update(long id, Task existingTask) {
        if (existingTask.title.length() == 0) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (existingTask.content.length() == 0) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        if (existingTask.dateTime == null) {
            throw new IllegalArgumentException("Date must be selected");
        }
        return taskRepository.update(id, existingTask);
    }
}
