package dev.psulej.taskapp.task;

import java.time.LocalDateTime;

public class TaskRequest {
    String title;
    String content;
    LocalDateTime dateTime;

    public TaskRequest(String title, String content, LocalDateTime dateTime) {
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}

