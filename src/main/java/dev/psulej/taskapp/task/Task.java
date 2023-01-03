package dev.psulej.taskapp.task;

import java.time.LocalDateTime;

public class Task {
    Long id;
    String title;
    String content;
//    LocalDateTime time;

    public Task(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
//        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

//    public LocalDateTime getTime() { return time; }
}
