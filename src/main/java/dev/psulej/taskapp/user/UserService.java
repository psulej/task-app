package dev.psulej.taskapp.user;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    public final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userValidator = userValidator;
    }

    public Long getLoggedUserId() {
        LoggedInUser loggedInUser = getLoggedUser();
        return loggedInUser.getId();
    }

    public LoggedInUser getLoggedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("User is not logged in");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof ApplicationUser)) {
            throw new IllegalStateException("Principal is not of type ApplicationUser");
        }

        ApplicationUser applicationUser = (ApplicationUser) principal;
        return new LoggedInUser(applicationUser.getId(),applicationUser.getLogin(),applicationUser.getEmail());
    }

    public void register(RegistrationRequest request) {
        userValidator.validate(request);

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
