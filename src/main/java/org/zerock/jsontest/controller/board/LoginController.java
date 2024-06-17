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

        if(bindingResult.hasErrors() || !(loginService.login(loginDTO))) {
            redirectAttributes.addFlashAttribute("message", "아이디 혹은 비밀번호가 잘못되었습니다.");
            return "redirect:/user/login";
        }

        if(loginService.login(loginDTO)){
            SignUpDTO signUpDTO = loginService.searchOne(loginDTO.getId());
            session.setAttribute("loginSession", signUpDTO.getId());
            if(redirectURL != null && !redirectURL.isEmpty()){
                return "redirect:" + redirectURL;
            }else{
                return "redirect:/board/list";
            }
        }

        return "/user/login";

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
    public void userInfoLogin(HttpSession session, Model model) {
        String loginSession = (String) session.getAttribute("loginSession");
        SignUpDTO signUpDTO = loginService.searchOne(loginSession);

        model.addAttribute("loginSession", signUpDTO);

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
        SignUpDTO signUpDTO = loginService.searchOne(loginId);
        model.addAttribute("userInfoId", signUpDTO);
        return "/user/modifyuserinfo";
    }


    @PostMapping("/user/modifyuserinfo")
    public String modifyuserinfo(@Valid SignUpDTO signUpDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/user/modifyuserinfo";
        }

        else {
            redirectAttributes.addFlashAttribute("complete", "정보가 수정되었습니다. 재 로그인 해주세요");
            loginService.modify(signUpDTO);
            return "redirect:/user/logout";
        }

    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session, Model model) {
        // 세션에서 로그인 ID 삭제
//        session.removeAttribute("loginSession");
        // 세션 무효화 (옵션)
        session.invalidate();
        model.addAttribute("complete", "로그아웃되었습니다.");
        // 로그아웃 후 로그인 페이지로 리다이렉트
        return "/user/logout";
    }

    @PostMapping("/user/logout")
    public String logout(HttpSession session) {

        // 세션에서 로그인 ID 삭제
//        session.removeAttribute("loginSession");
        // 세션 무효화 (옵션)
        session.invalidate();
        // 로그아웃 후 로그인 페이지로 리다이렉트
        return "redirect:/board/list";
    }

}


