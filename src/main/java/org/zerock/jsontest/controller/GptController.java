//package org.zerock.jsontest.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.zerock.jsontest.service.GptService;
//
//@Controller
//@RequestMapping("/api/v1/gpt")
//public class GptController {
//    private final GptService gptService;
//
//    @Autowired
//    public GptController(GptService gptService) {
//        this.gptService = gptService;
//    }
//
//    @GetMapping
//    public String chat() {
//        return "chat";
//    }
//
//    @PostMapping
//    @ResponseBody
//    public ResponseEntity<?> getAssistantMsg(@RequestParam String msg) throws JsonProcessingException {
//        return gptService.getAssistantMsg(msg);
//    }
//}
