package com.school.what_is_your_ootd.controller;

import com.school.what_is_your_ootd.dto.RecommendRequest;
import com.school.what_is_your_ootd.vo.Style;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    private final Dotenv dotenv;

    @Autowired
    public HomeController(Dotenv dotenv) {
        this.dotenv = dotenv;
    }

    @GetMapping
    public String home(Model model) {
        model.addAttribute("form", new RecommendRequest());
        model.addAttribute("styles", Style.values());
        model.addAttribute("API_KEY", dotenv.get("KAKAO_MAP_API_KEY"));
        return "home";
    }
}
