package dev.psulej.taskapp.task;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class TaskRowMapper implements RowMapper<Task> {
    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        String title = rs.getString("title");
        String content = rs.getString("content");
        LocalDateTime dateTime = rs.getTimestamp("date_time").toLocalDateTime();
        return new Task(id, title, content, dateTime);
    }
}
