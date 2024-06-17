package org.zerock.jsontest.controller.board;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
//import org.zerock.b01.dto.SessionDTO;

@ControllerAdvice
@Log4j2
@RequiredArgsConstructor
public class CommonControllerAdvice {

    @ModelAttribute("loginSession")
    public String addLoginSessionToModel(HttpSession session) {

        return (String) session.getAttribute("loginSession");
    }
}
