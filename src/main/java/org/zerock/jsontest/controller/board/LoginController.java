package org.zerock.jsontest.controller.board;


import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.jsontest.dto.board.LoginDTO;
import org.zerock.jsontest.dto.board.SignUpDTO;
import org.zerock.jsontest.service.board.LoginService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/user/login")
    public void login() {

    }

    @PostMapping("/user/login")
    public String logincheck(@Valid LoginDTO loginDTO, BindingResult bindingResult,
                             RedirectAttributes redirectAttributes, HttpSession session , @RequestParam String redirectURL){

        if(bindingResult.hasErrors() || !loginService.login(loginDTO)) {
            redirectAttributes.addFlashAttribute("message", "아이디 혹은 비밀번호가 잘못되었습니다.");
            return "redirect:/user/login";
        }

        Optional<SignUpDTO> optionalSignUpDTO = loginService.searchOne(loginDTO.getId()); // 변경된 부분
        if (optionalSignUpDTO.isPresent()) { // 변경된 부분
            SignUpDTO signUpDTO = optionalSignUpDTO.get(); // 변경된 부분
            session.setAttribute("loginSession", signUpDTO.getId());
            if (redirectURL != null && !redirectURL.isEmpty()) {
                return "redirect:" + redirectURL;
            } else {
                return "redirect:/board/list";
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "사용자를 찾을 수 없습니다."); // 변경된 부분
            return "redirect:/user/login"; // 변경된 부분
        }
    }

    @GetMapping("/user/signup")
    public void signup() {

    }

    @PostMapping("/user/signup")
    public String signupcheck(@Valid SignUpDTO signUpDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/user/signup";
        }
        else if(!(loginService.register(signUpDTO))){
            redirectAttributes.addFlashAttribute("false2", "중복된 아이디가 있습니다.");
            return "redirect:/user/signup";
        }

        else {
            redirectAttributes.addFlashAttribute("complete", "회원가입이 완료되었습니다.");
            return "redirect:/user/login";
        }

    }

    @GetMapping("/user/userinfologin")
    public String userInfoLogin(HttpSession session, Model model) { // 변경된 부분
        String loginSession = (String) session.getAttribute("loginSession");
        Optional<SignUpDTO> optionalSignUpDTO = loginService.searchOne(loginSession); // 변경된 부분

        if (optionalSignUpDTO.isPresent()) { // 변경된 부분
            model.addAttribute("loginSession", optionalSignUpDTO.get()); // 변경된 부분
            return "user/userinfologin"; // 변경된 부분
        } else {
            model.addAttribute("message", "사용자를 찾을 수 없습니다."); // 변경된 부분
            return "redirect:/user/login"; // 변경된 부분
        }
    }

    @PostMapping("user/userinfologin")
    public String checkuserInfoLogin(@Valid LoginDTO loginDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if(bindingResult.hasErrors() || !(loginService.login(loginDTO))) {
            redirectAttributes.addFlashAttribute("message", "비밀번호가 잘못되었습니다.");
            return "redirect:/user/userinfologin";
        }

        return "redirect:/user/modifyuserinfo";
    }

    @GetMapping("user/modifyuserinfo")
    public String modifyCheckuserinfo(HttpSession session, Model model) {

        String loginId = (String) session.getAttribute("loginSession");
        Optional<SignUpDTO> optionalSignUpDTO = loginService.searchOne(loginId); // 변경된 부분

        if (optionalSignUpDTO.isPresent()) {
            model.addAttribute("userInfoId", optionalSignUpDTO.get()); // 변경된 부분
            return "user/modifyuserinfo";
        } else {
            model.addAttribute("message", "사용자를 찾을 수 없습니다."); // 변경된 부분
            return "redirect:/user/login";
        }
    }



    @PostMapping("/user/modifyuserinfo")
    public String modifyuserinfo(@Valid SignUpDTO signUpDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:user/modifyuserinfo";
        }

        else {
            redirectAttributes.addFlashAttribute("complete", "정보가 수정되었습니다. 재 로그인 해주세요");
            loginService.modify(signUpDTO);
            return "redirect:/user/logout";
        }

    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session, Model model) {
        // 세션 무효화
        session.invalidate();
        model.addAttribute("complete", "로그아웃되었습니다.");
        // 로그아웃 후 메인 페이지로 리다이렉트
        return "redirect:/board/list"; // 또는 "redirect:/user/login" 으로 변경 가능
    }

    @PostMapping("/user/logout")
    public String logoutPost(HttpSession session) {
        // 세션 무효화
        session.invalidate();
        // 로그아웃 후 메인 페이지로 리다이렉트
        return "redirect:/board/list"; // 또는 "redirect:/user/login" 으로 변경 가능
    }

}


