package org.zerock.jsontest.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.jsontest.service.RecommendationService;

@RequestMapping("/bigdatarecommendation")
@Controller
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/main")
    public String home(HttpSession session, Model model) {
        String loginSession = (String) session.getAttribute("loginSession");
        model.addAttribute("loginSession", loginSession);
        return "bigdatarecommendation/main";
    }

    @PostMapping("/recommend")
    public String recommend(@RequestParam("userid") String userid, Model model) {
        try {
            String[] recommendations = recommendationService.getRecommendations(userid);
            model.addAttribute("recommendations", recommendations);
            return "bigdatarecommendation/result";
        } catch (IndexOutOfBoundsException e) {
            model.addAttribute("error", "User not found");
            return "bigdatarecommendation/error";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "bigdatarecommendation/error";
        }
    }
}
