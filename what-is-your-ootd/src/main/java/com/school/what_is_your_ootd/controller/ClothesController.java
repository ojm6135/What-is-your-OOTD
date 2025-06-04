package com.school.what_is_your_ootd.controller;

import com.school.what_is_your_ootd.dto.ClothingItemDto;
import com.school.what_is_your_ootd.service.ClothesService;
import com.school.what_is_your_ootd.vo.ClothesType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users/{username}/clothes")
public class ClothesController {
    private ClothesService clothesService;

    @Autowired
    public ClothesController(ClothesService clothesService) {
        this.clothesService = clothesService;
    }

    @PostMapping
    public String addClothingItem(@PathVariable(name = "username") String username,
                                  @ModelAttribute ClothingItemDto clothingItemDto) {
        clothesService.addClothingItem(username, clothingItemDto);
        return "redirect:/users/" + username + "/clothes";
    }

    @GetMapping
    public String viewMyClothes(@PathVariable(name = "username") String username,
                                Model model) {
        List<ClothingItemDto> items = clothesService.findAllByUsername(username);
        model.addAttribute("items", items);
        model.addAttribute("addedItem", new ClothingItemDto());
        model.addAttribute("itemTypes", ClothesType.values());
        return "my-clothes";
    }

    @DeleteMapping("/{itemId}")
    public String deleteClothingItem(@PathVariable(name = "username") String username,
                                     @PathVariable(name = "itemId") Long itemId) {
        clothesService.deleteClothingItem(username, itemId);
        return "redirect:/users/" + username + "/clothes";
    }
}
