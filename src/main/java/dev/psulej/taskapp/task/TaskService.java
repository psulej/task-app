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

    private static String getOrderByParameter(String sort) {
        Map<String, String> orderByColumns = new HashMap<>();
        orderByColumns.put("id", "id");

        String sortColumnName = orderByColumns.getOrDefault(sort, "id");
        return sortColumnName;
    }


    private NamedParameterJdbcTemplate jdbcTemplate;

    public TaskService(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public PaginationResponse<Task> getTasks(int page,int size,String sort) {
        //String sql = "SELECT id, title, content FROM tasks ORDER BY id";

        String sql = "SELECT id, title, content FROM tasks WHERE 1 = 1";
        String countSql = "SELECT count(*) FROM tasks WHERE 1 = 1";

        Map<String, Object> parameters = new HashMap<>();

        String sortColumnName = getOrderByParameter(sort);
        sql += " ORDER BY " + sortColumnName;
        sql += " LIMIT " + size;
        sql += " OFFSET  " + page * size;

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

        RowMapper<Long> countRowMapper = new RowMapper<>() {
            @Override
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong(1);
            }
        };

        Long totalItems = jdbcTemplate.queryForObject(countSql, parameters, countRowMapper);
        long totalPages = (long) (Math.ceil(totalItems / (size * 1.0)));

        int currentPage = page;

        PaginationResponse<Task> response = new PaginationResponse<>(totalItems, totalPages, currentPage,tasks);
        return response;
    }

    public Task getTask(long id) {
        String sql = "SELECT id, title, content FROM tasks WHERE id = :id";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);

        RowMapper<Task> taskRowMapper = new RowMapper<Task>() {
            @Override
            public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
                long id = rs.getLong("id");
                String title = rs.getString("title");
                String content = rs.getString("content");

                return new Task(id, title, content);
            }
        };

        Task task = jdbcTemplate.queryForObject(sql, parameters, taskRowMapper);
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
