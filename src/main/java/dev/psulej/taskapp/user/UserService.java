package dev.psulej.taskapp.user;
import dev.psulej.taskapp.error.ValidationError;
import dev.psulej.taskapp.error.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegistrationRequest request) {
        validate(request);

        String hashedPassword = passwordEncoder.encode(request.password);

        User newUser = new User(
                null,
                request.login,
                hashedPassword,
                request.email
        );

        userRepository.create(newUser);
    }

    private void validate(RegistrationRequest request) {
        List<ValidationError> errors = new ArrayList<>();

        if (userRepository.emailExists(request.email)) {
            errors.add(ValidationError.EMAIL_EXISTS);
        }
        if (userRepository.loginExists(request.login)) {
            errors.add(ValidationError.LOGIN_EXISTS);
        }

        throw new ValidationException(errors);
    }
}
