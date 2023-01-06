package dev.psulej.taskapp.user;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(Model model,
                            @RequestParam(name = "error", required = false) String error,
                            @RequestParam(name = "logout", required = false) String logout,
                            @RequestParam(name = "register", required = false) String register) {
        if (error != null) {
            model.addAttribute("error");
        }
        if (register != null) {
            model.addAttribute("register");
        }
        if (logout != null) {
            model.addAttribute("logout");
        }
        return "login";
    }
}
