package dev.psulej.taskapp.task;

import org.springframework.stereotype.Service;

@Service
public class TaskValidator {

    public TaskValidator() {
    }

    public void validate(Task newTask) {
        if (newTask.title.length() == 0) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (newTask.content.length() == 0) {
            throw new IllegalArgumentException("Content cannot be empty");
        }
        if (newTask.dateTime == null) {
            throw new IllegalArgumentException("Date must be selected");
        }
    }
}
