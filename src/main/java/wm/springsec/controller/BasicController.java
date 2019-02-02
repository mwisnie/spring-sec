package wm.springsec.controller;

import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/doSomeAdminStuff")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String doSomeAdminStuff() {
        System.out.println("Doing something that only user with ADMIN role should be able to do.");
        return "admin";
    }


}
