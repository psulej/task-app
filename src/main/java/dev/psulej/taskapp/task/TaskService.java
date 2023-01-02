package dev.psulej.taskapp.task;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public TaskService(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Task> getTasks() {
        String sql = "SELECT id, title, content FROM tasks ORDER BY id";
        Map<String, Object> parameters = new HashMap<>();

        RowMapper<Task> rowMapper = new RowMapper<>() {
            @Override
            public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String content = rs.getString("content");
                return new Task(id, title, content);
            }
        };

        List<Task> tasks = jdbcTemplate.query(sql, parameters, rowMapper);

        return tasks;
    }

    public Task getTask(long id) {
        String sql = "SELECT id, title, content FROM tasks WHERE id = :id";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);

        RowMapper<Task> userRowMapper = new RowMapper<Task>() {
            @Override
            public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String content = rs.getString("content");

                return new Task(id, title, content);
            }
        };

        Task task = jdbcTemplate.queryForObject(sql, parameters, userRowMapper);
        return task;
    }

    public Task createTask(Task newTask) {
        if (newTask.title.length() == 0) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (newTask.content.length() == 0) {
            throw new IllegalArgumentException("Content cannot be empty");
        } else {

            String sql = """
                    INSERT INTO tasks(id, title, content) 
                    VALUES (nextval('tasks_seq'), :title, :content)
                    """;

            HashMap<String, Object> parameters = new HashMap<>();

            parameters.put("title", newTask.title);
            parameters.put("content", newTask.content);

            KeyHolder key = new GeneratedKeyHolder(); // zwraca id dla taska
            jdbcTemplate.update(sql, new MapSqlParameterSource(parameters), key, new String[]{"id"});

            long taskId = key.getKey().longValue();
            return new Task(taskId, newTask.title, newTask.content);
        }
    }

    public void deleteTask(long id) {
        String sql = "DELETE FROM tasks WHERE id = :id";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        jdbcTemplate.update(sql, parameters);
    }

    public Task updateTask(long id,Task existingTask) {
        String sql = "UPDATE tasks SET title = :title, content = :content WHERE id = :id";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id",id);
        parameters.put("title",existingTask.title);
        parameters.put("content",existingTask.content);
        jdbcTemplate.update(sql, parameters);

        return new Task(
                id,
                existingTask.title,
                existingTask.content
        );
    }
}
