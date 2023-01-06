package dev.psulej.taskapp.user;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class RegistrationRequest {

    @NotEmpty
    @Pattern(message = "Login is invalid", regexp = "[A-Za-z0-9]{3,14}")
    String login;

    @NotEmpty
    @Pattern(message = "Password is invalid", regexp = "[A-Za-z0-9]{5,14}")
    String password;

    @NotEmpty
    @Pattern(message = "Email is invalid", regexp = "^(.+)@(\\S+)$")
    String email;

    public RegistrationRequest(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
