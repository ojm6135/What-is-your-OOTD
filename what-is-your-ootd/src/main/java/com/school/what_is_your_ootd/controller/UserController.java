package com.school.what_is_your_ootd.controller;

import com.school.what_is_your_ootd.form.UserRegistrationForm;
import com.school.what_is_your_ootd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sign-up")
    public String signupPage(Model model) {
        model.addAttribute("form", new UserRegistrationForm());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signup(@ModelAttribute UserRegistrationForm form) {
        boolean result = userService.signUp(form);
        if (!result) {
            return "redirect:/users/signup";
        }

        return "redirect:/";
    }
}