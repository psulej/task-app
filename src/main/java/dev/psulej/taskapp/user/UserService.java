package dev.psulej.taskapp.user;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegistrationRequest request) {
        // sprawdzenie czy uzytkownik nie istnieje juz w bazie (dla loginu i emaila)
        // przemapowianie requesta na encje (obiekt domenowy)
        if (userRepository.validateEmail(request.email)) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.validateLogin(request.login)) {
            throw new IllegalArgumentException("Login already exists");
        }

        String hashedPassword = passwordEncoder.encode(request.password);

        User newUser = new User(
                null,
                request.login,
                hashedPassword,
                request.email
        );

        userRepository.create(newUser);
    }
}
