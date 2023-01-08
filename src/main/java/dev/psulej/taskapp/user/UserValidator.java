package dev.psulej.taskapp.user;
import dev.psulej.taskapp.common.error.ValidationError;
import dev.psulej.taskapp.common.error.ValidationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserValidator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    void validate(RegistrationRequest request) {
        List<ValidationError> errors = new ArrayList<>();

        if (userRepository.emailExists(request.email)) {
            errors.add(ValidationError.EMAIL_EXISTS);
        }
        if (userRepository.loginExists(request.login)) {
            errors.add(ValidationError.LOGIN_EXISTS);
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
}



