package application.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
@RequestMapping("/")
public class Controller {

    @GetMapping("/login")
    public String loginPage(Model model) {

        return "login";
    }

    @GetMapping("/loginerror")
    public String loginPageError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }



}
