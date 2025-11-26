package ru.Golov_Denis.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.Golov_Denis.NauJava.entity.UserEntity;
import ru.Golov_Denis.NauJava.service.UserService;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(String username, String email, String password, Model model) {
        UserEntity user = userService.addUser(username, email, password);
        if (user == null) {
            model.addAttribute("message", "Пользователь с таким именем уже существует");
            return "registration";
        }
        return "redirect:/login";
    }
}
