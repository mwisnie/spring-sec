package wm.springsec.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BasicController {

    @GetMapping("/")
    public String getHome() {
        return "home";
    }

    @GetMapping("/everyone")
    public String getEveryone() {
        return "everyone";
    }

    @GetMapping("/management")
    public String getManagement() {
        return "management";
    }

    @GetMapping("/admin")
    public String getAdmin() {
        return "admin";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login-page";
    }

    @GetMapping("/access-denied")
    public String getAccessDenied() {
        return "access-denied";
    }

}
