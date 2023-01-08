package dev.psulej.taskapp.user;

public class LoggedInUser {
   private final long id;
   private final String login;
   private final String email;

    public LoggedInUser(long id, String login, String email) {
        this.id = id;
        this.login = login;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }
}
