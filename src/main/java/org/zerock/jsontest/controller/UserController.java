package org.zerock.jsontest.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.jsontest.domain.User;
import org.zerock.jsontest.dto.UserDTO;
import org.zerock.jsontest.service.UserService;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor

public class UserController {
    private final UserService userService;
    private final HttpSession httpSession;

    @GetMapping("/signup")
    public void signup(){}

    @PostMapping("/signup")
    public String signuppost(@Valid UserDTO userDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/user/signup";
        }
        String id = userService.signup(userDTO);
        redirectAttributes.addFlashAttribute("message", "User registered successfully");
        return "redirect:/home";
    }
//    로그인 구현
    @GetMapping("/login")
    public  void login(){

    }

@PostMapping("/login")
    public String login(@RequestParam String id,
                        @RequestParam String password,
                        RedirectAttributes redirectAttributes,
                        HttpServletRequest request) {

        try {
            UserDTO userDTO = userService.login(id, password);
            request.getSession().setAttribute("loggedInUser", userDTO); // 세션에 로그인 사용자 정보 저장
            return "redirect:/home"; // 로그인 성공 후 홈 페이지로 리디렉션
        } catch (NoSuchElementException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid credentials. Please try again.");
            return "redirect:/user/login"; // 로그인 실패 시 로그인 페이지로 리디렉션
        }
    }
}
