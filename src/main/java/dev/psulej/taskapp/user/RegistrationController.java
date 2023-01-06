package dev.psulej.taskapp.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public void register(@Valid @RequestBody RegistrationRequest request) {
        userService.register(request);
    }

}
