package dev.psulej.taskapp.task;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<Task> getTasks(){
        String sql = "SELECT id, title, content FROM tasks";
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

    public Task createTask(Task newTask) {
        String sql = """
        INSERT INTO tasks(id, title, content) 
        VALUES (nextval('tasks_seq'), :title, :content)
        """;

        HashMap<String, Object> parameters = new HashMap<>();

        parameters.put("title", newTask.title);
        parameters.put("content", newTask.content);

        KeyHolder key = new GeneratedKeyHolder(); // zwraca id dla taska
        jdbcTemplate.update(sql, new MapSqlParameterSource(parameters), key, new String[] { "id" });

        long taskId = key.getKey().longValue();
        return new Task(taskId, newTask.title, newTask.content);
    }

    public void deleteTask(long id) {
        String sql = "DELETE FROM tasks WHERE id = :id";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        jdbcTemplate.update(sql, parameters);
    }
}
