package com.school.what_is_your_ootd.controller;

import com.school.what_is_your_ootd.dto.UserDto;
import com.school.what_is_your_ootd.form.UserRegistrationForm;
import com.school.what_is_your_ootd.service.UserService;
import com.school.what_is_your_ootd.vo.Location;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final Dotenv dotenv;

    @Autowired
    public UserController(UserService userService, Dotenv dotenv) {
        this.userService = userService;
        this.dotenv = dotenv;
    }

    @GetMapping("/sign-up")
    public String signupPage(Model model) {
        model.addAttribute("API_KEY", dotenv.get("KAKAO_MAP_API_KEY"));
        model.addAttribute("form", new UserRegistrationForm());
        return "sign-up";
    }

    @PostMapping("/sign-up")
    public String signup(@ModelAttribute UserRegistrationForm form) {
        if (!userService.signUp(form)) {
            return "redirect:/users/sign-up";
        }

        return "redirect:/users/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/profile/{username}")
    public String viewMyInfo(@PathVariable(name = "username") String username, Model model) {
        Optional<UserDto> userDtoOptional = userService.findByUsername(username);
        if (userDtoOptional.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("API_KEY", dotenv.get("KAKAO_MAP_API_KEY"));
        model.addAttribute("user", userDtoOptional.get());
        return "my-info";
    }

    @PutMapping("/profile/{username}")
    public String updateMyInfo(@PathVariable(name = "username") String username,
                               @ModelAttribute Location location) {
        userService.updateInfo(username, location);
        return "redirect:/users/profile/" + username;
    }
}