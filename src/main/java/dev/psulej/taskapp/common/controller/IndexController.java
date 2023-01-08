package dev.psulej.taskapp.common.controller;

import dev.psulej.taskapp.user.LoggedInUser;
import dev.psulej.taskapp.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String index(Model model) {
        LoggedInUser loggedUser = userService.getLoggedUser();
        model.addAttribute("user", loggedUser);
        return "index";
    }
}
