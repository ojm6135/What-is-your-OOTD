package com.school.what_is_your_ootd.controller;

import com.school.what_is_your_ootd.dto.OutfitDto;
import com.school.what_is_your_ootd.dto.RecommendRequest;
import com.school.what_is_your_ootd.service.OutfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class OutfitController {
    private OutfitService outfitService;

    @Autowired
    public OutfitController(OutfitService outfitService) {
        this.outfitService = outfitService;
    }


    @PostMapping("/outfits/recommend")
    public String recommendOutfit(@ModelAttribute RecommendRequest request,
                                  Model model) {
        Optional<OutfitDto> resultOptional = outfitService.recommendOutfit(request);
        if (resultOptional.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("outfit", resultOptional.get());
        return "result";
    }

    @PostMapping("/users/{username}/outfits")
    @ResponseBody
    public boolean saveOutfit(@PathVariable(name = "username") String username,
                              @ModelAttribute OutfitDto outfitDto) {
        return outfitService.save(username, outfitDto);
    }
}