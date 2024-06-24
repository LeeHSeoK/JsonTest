package org.zerock.jsontest.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.zerock.jsontest.dto.TravelDTO;
import org.zerock.jsontest.repository.DbRepository;
import org.zerock.jsontest.service.GptService;
import org.zerock.jsontest.service.dbService.DbService;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class GptController {

    @Autowired
    private RestTemplate restTemplate;

    final DbService dbService;

    @GetMapping("/chat")
    public String showChatPage() {
        return "chat";
    }

    @PostMapping("/ask")
    @ResponseBody
    public Map<String, String> askQuestion(@RequestBody QueryRequest queryRequest) {
        String url = "http://localhost:8000/ask";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<QueryRequest> request = new HttpEntity<>(queryRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        System.out.println(response);
        String testresponse = response.getBody();
        System.out.println(testresponse);

        String extractedPlace = extractPlaceName(testresponse);
        System.out.println("Extracted Place: " + extractedPlace);

        TravelDTO travelDTO = dbService.searchOne(extractedPlace);
        System.out.println(travelDTO.toString());

        Map<String, String> result = new HashMap<>();
        result.put("response", testresponse);
        result.put("contentid", travelDTO.getContentid());
        result.put("contenttypeid", travelDTO.getContenttypeid());

        return result;
    }

    private String extractPlaceName(String response) {
        String pattern = "추천장소 바로가기 : (\\S+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(response);
        if (m.find()) {
            String place = m.group(1);
            if (place.startsWith("\"")) {
                place = place.substring(1);
            }
            if (place.endsWith("\"")) {
                place = place.substring(0, place.length() - 1);
            }
            return place;
        } else {
            return "No match found";
        }
    }
}

class QueryRequest {

    private String question;

    public QueryRequest() {}

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

}
