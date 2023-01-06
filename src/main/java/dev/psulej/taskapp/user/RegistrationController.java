package dev.psulej.taskapp.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public void register(@Valid @RequestBody RegistrationRequest request) {
        userService.register(request);
    }
}

@Controller
class RegistrationViewController {

    @GetMapping("/registration")
    public String registrationPage() {
        return "register";
    }
}
