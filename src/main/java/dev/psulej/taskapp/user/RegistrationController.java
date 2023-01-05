package dev.psulej.taskapp.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/registration")
@Controller
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String registrationPage() {
        return "register";
    }

    @PostMapping
    @ResponseBody
    public void register(@RequestBody RegistrationRequest request) {
        userService.register(request);
    }
}
