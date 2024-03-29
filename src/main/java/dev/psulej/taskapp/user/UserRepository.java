package dev.psulej.taskapp.user;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

@Repository
public class UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    public UserRepository(NamedParameterJdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRowMapper = userRowMapper;
    }

    public User create(User newUser) {
        String sql = """
                INSERT INTO users(id, login, password, email) 
                VALUES (nextval('users_seq'), :login, :password, :email)
                """;

        HashMap<String, Object> parameters = new HashMap<>();

        parameters.put("login", newUser.login);
        parameters.put("password", newUser.password);
        parameters.put("email", newUser.email);

        KeyHolder key = new GeneratedKeyHolder(); // zwraca id dla usera
        jdbcTemplate.update(sql, new MapSqlParameterSource(parameters), key, new String[]{"id"});
        long userId = key.getKey().longValue();

        return new User(userId, newUser.login, newUser.password, newUser.email);
    }

    public User findByLogin(String username) {
        String sql = "select id, login, password, email from users where lower(:login) = lower(login)";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("login", username);

        return jdbcTemplate.query(sql, parameters, userRowMapper)
                .stream()
                .findFirst()
                .orElse(null);
    }

    public boolean emailExists(String email) {

        boolean emailExists = false;

        String sql = "select exists(select 1 from users where email = :email)";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("email", email);

        RowMapper<Boolean> rowMapper = new RowMapper<>() {
            @Override
            public Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getBoolean(1);
            }
        };

        emailExists = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, parameters, rowMapper));
        return emailExists;
    }

    public boolean loginExists(String login) {
        boolean loginExists = false;

        String sql = "select exists(select 1 from users where login = :login)";
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("login", login);

        RowMapper<Boolean> rowMapper = new RowMapper<>() {
            @Override
            public Boolean mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getBoolean(1);
            }
        };

        loginExists = Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, parameters, rowMapper));
        return loginExists;
    }
}
