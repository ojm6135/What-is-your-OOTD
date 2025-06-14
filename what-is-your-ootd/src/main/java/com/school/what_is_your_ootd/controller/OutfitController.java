package com.school.what_is_your_ootd.controller;

import com.school.what_is_your_ootd.dto.OutfitDto;
import com.school.what_is_your_ootd.dto.RecommendRequest;
import com.school.what_is_your_ootd.service.OutfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class OutfitController {
    private OutfitService outfitService;
    private final int pageSize;

    @Autowired
    public OutfitController(OutfitService outfitService) {
        this.outfitService = outfitService;
        this.pageSize = 9;
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

    @GetMapping("/users/{username}/outfits")
    public String viewMyOutfits(@PathVariable(name = "username") String username,
                                @RequestParam("page") Optional<Integer> page,
                                Model model) {
        int currentPage = page.orElse(1);

        Page<OutfitDto> outfitPage = outfitService.findAllByUserId(
                username,
                PageRequest.of(currentPage - 1, pageSize)
        );

        model.addAttribute("outfitPage", outfitPage);

        int totalPages = outfitPage.getTotalPages();

        if (totalPages > 0) {
            // 10개의 페이지 표시, ex) 1 ~ 10, 11 ~ 20
            int from = currentPage - (currentPage % 10) + 1;
            int to = from + 9;

            List<Integer> pageNumbers = IntStream.rangeClosed(from, Math.min(to, totalPages))
                    .boxed()
                    .collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);

            int prevPageNumber = from - 1;
            int nextPageNumber = to + 1;

            if (nextPageNumber > totalPages) {
                nextPageNumber = -1;
            }

            model.addAttribute("prevPageNumber", prevPageNumber);
            model.addAttribute("nextPageNumber", nextPageNumber);
        }

        int rowCnt = (int) Math.ceil((double) outfitPage.getNumberOfElements() / 3);

        model.addAttribute("rowCnt", rowCnt);
        model.addAttribute("elementCnt", outfitPage.getNumberOfElements());

        return "my-outfits";
    }

    @GetMapping("/outfits/browse")
    public String viewPublicOutfits(@RequestParam("page") Optional<Integer> page,
                                    Model model) {
        int currentPage = page.orElse(1);

        Page<OutfitDto> outfitPage = outfitService.findAllPublicOutfits(
                PageRequest.of(currentPage - 1, pageSize)
        );

        model.addAttribute("outfitPage", outfitPage);

        int totalPages = outfitPage.getTotalPages();

        if (totalPages > 0) {
            // 10개의 페이지 표시, ex) 1 ~ 10, 11 ~ 20
            int from = currentPage - (currentPage % 10) + 1;
            int to = from + 9;

            List<Integer> pageNumbers = IntStream.rangeClosed(from, Math.min(to, totalPages))
                    .boxed()
                    .collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);

            int prevPageNumber = from - 1;
            int nextPageNumber = to + 1;

            if (nextPageNumber > totalPages) {
                nextPageNumber = -1;
            }

            model.addAttribute("prevPageNumber", prevPageNumber);
            model.addAttribute("nextPageNumber", nextPageNumber);
        }

        int rowCnt = (int) Math.ceil((double) outfitPage.getNumberOfElements() / 3);

        model.addAttribute("rowCnt", rowCnt);
        model.addAttribute("elementCnt", outfitPage.getNumberOfElements());

        return "browse";
    }

    @PatchMapping("/users/{username}/outfits/{outfitId}")
    public String toggleOutfitStatus(@PathVariable(name = "username") String username,
                                     @PathVariable(name = "outfitId") Long outfitId) {
        outfitService.toggleOutfitStatus(username, outfitId);
        return "redirect:/users/" + username + "/outfits";
    }

    @DeleteMapping("/users/{username}/outfits/{outfitId}")
    public String deleteOutfit(@PathVariable(name = "username") String username,
                               @PathVariable(name = "outfitId") Long outfitId) {
        outfitService.deleteOutfit(username, outfitId);
        return "redirect:/users/" + username + "/outfits";
    }
}