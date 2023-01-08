package dev.psulej.taskapp.task;

import java.time.LocalDateTime;

public class Task {
    long id;
    String title;
    String content;
    LocalDateTime dateTime;

    public Task(Long id, String title, String content,LocalDateTime time) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateTime = time;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getDateTime() { return dateTime; }
}
