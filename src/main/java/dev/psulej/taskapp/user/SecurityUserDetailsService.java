package dev.psulej.taskapp.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

public class SecurityUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public SecurityUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username);
        return new ApplicationUser(user.id, user.login, user.email, user.password);
    }
}

class ApplicationUser extends org.springframework.security.core.userdetails.User {

    private final long id;
    private final String email;

    ApplicationUser(long id, String login, String email, String password) {
        super(
                login,
                password,
                true,
                true,
                true,
                true,
                Set.of()
        );
        this.id = id;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return getUsername();
    }

    public String getEmail() {
        return email;
    }

}