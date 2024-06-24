package org.zerock.jsontest.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;



@Controller
@RequestMapping("/api")
public class GptController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/chat")
    public String showChatPage() {
        return "chat";
    }

    @PostMapping("/ask")
    @ResponseBody
    public String askQuestion(@RequestBody QueryRequest queryRequest) {
        String url = "http://localhost:8000/ask";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<QueryRequest> request = new HttpEntity<>(queryRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        System.out.println(response);
        String testresponse = response.getBody();

        return response.getBody();

    }
}

class QueryRequest {

    private String question;

    // 기본 생성자
    public QueryRequest() {}

    // Getter 및 Setter
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

}
