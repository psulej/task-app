package dev.psulej.taskapp.task;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
class TaskRepository {

    private final TaskRowMapper taskRowMapper;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public TaskRepository(TaskRowMapper taskRowMapper, NamedParameterJdbcTemplate jdbcTemplate) {
        this.taskRowMapper = taskRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }


    public PaginationResponse<Task> getPage(long userId, int page, int size, String sort) {

        String sql = "SELECT id, title, content, date_time, user_id FROM tasks WHERE user_id = :userId";
        String countSql = "SELECT count(*) FROM tasks WHERE user_id = :userId";

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("userId",userId);

        String sortColumnName = getOrderByParameter(sort);
        sql += " ORDER BY " + sortColumnName;
        sql += " LIMIT " + size;
        sql += " OFFSET  " + page * size;

        List<Task> tasks = jdbcTemplate.query(sql, parameters, taskRowMapper);

        RowMapper<Long> countRowMapper = new RowMapper<>() {
            @Override
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong(1);
            }
        };

        Long totalItems = jdbcTemplate.queryForObject(countSql, parameters, countRowMapper);
        long totalPages = (long) (Math.ceil(totalItems / (size * 1.0)));

        int currentPage = page;

        PaginationResponse<Task> response = new PaginationResponse<>(totalItems, totalPages, currentPage, tasks);
        return response;
    }


    public Task get(long id) {
        String sql = "SELECT id, title, content, date_time FROM tasks WHERE id = :id";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);

        Task task = jdbcTemplate.queryForObject(sql, parameters, taskRowMapper);
        return task;
    }

    public Task create(long userId, TaskRequest  newTask) {
        String sql = """
                INSERT INTO tasks(id, title, content, date_time, user_id) 
                VALUES (nextval('tasks_seq'), :title, :content, :dateTime, :userId)
                """;

        HashMap<String, Object> parameters = new HashMap<>();

        parameters.put("title", newTask.title);
        parameters.put("content", newTask.content);
        parameters.put("dateTime", newTask.dateTime);
        parameters.put("userId", userId);

        KeyHolder key = new GeneratedKeyHolder(); // zwraca id dla taska
        jdbcTemplate.update(sql, new MapSqlParameterSource(parameters), key, new String[]{"id"});

        long taskId = key.getKey().longValue();
        return new Task(taskId, newTask.title, newTask.content, newTask.dateTime);

    }

    public void delete(long id) {
        String sql = "DELETE FROM tasks WHERE id = :id";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        jdbcTemplate.update(sql, parameters);
    }

    public Task update(long id, long userId, TaskRequest  existingTask) {
        String sql = "UPDATE tasks SET title = :title, content = :content, date_time = :dateTime WHERE id = :id AND user_id = :userId";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("id", id);
        parameters.put("title", existingTask.title);
        parameters.put("content", existingTask.content);
        parameters.put("dateTime", existingTask.dateTime);
        parameters.put("userId", userId);
        jdbcTemplate.update(sql, parameters);

        return new Task(
                id,
                existingTask.title,
                existingTask.content,
                existingTask.dateTime
        );
    }

    private static String getOrderByParameter(String sort) {
        Map<String, String> orderByColumns = new HashMap<>();
        orderByColumns.put("id", "id");

        String sortColumnName = orderByColumns.getOrDefault(sort, "id");
        return sortColumnName;
    }

}
