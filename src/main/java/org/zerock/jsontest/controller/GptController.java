package org.zerock.jsontest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.jsontest.dto.TravelDTO;
import org.zerock.jsontest.service.dbService.DbService;

import java.io.IOException;
import java.time.Clock;
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

    @GetMapping("/chat/image")
    public String imageChatPage() {
        return "chat_image";
    }

    @PostMapping("/ask")
    @ResponseBody
    public Map<String, String> askQuestion(@RequestBody QueryRequest queryRequest) {
        String url = "http://localhost:8000/ask";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<QueryRequest> request = new HttpEntity<>(queryRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        String testresponse = response.getBody();
        String extractedPlace = extractPlaceName(testresponse);

        TravelDTO travelDTO = dbService.searchOne(extractedPlace);

        Map<String, String> result = new HashMap<>();
        result.put("response", testresponse);
        result.put("contentid", travelDTO.getContentid());
        result.put("contenttypeid", travelDTO.getContenttypeid());

        return result;
    }

    @PostMapping("/upload-image")
    @ResponseBody
    public Map<String, String> uploadImage(@RequestParam("image") MultipartFile image) throws IOException {
        String imageUrl = uploadImageToImgbb(image);
        if (imageUrl != null) {
            Map<String, String> response = new HashMap<>();
            response.put("image_url", imageUrl);
            return response;
        } else {
            throw new IOException("이미지 업로드에 실패했습니다.");
        }
    }

    @PostMapping("/ask-image")
    @ResponseBody
    public Map<String, String> askImageQuestion(@RequestBody ImageQueryRequest queryRequest) {
        String askUrl = "http://localhost:8000/ask/image";
        HttpHeaders askHeaders = new HttpHeaders();
        askHeaders.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> askBody = new HashMap<>();
        askBody.put("url", queryRequest.getUrl());
        askBody.put("message", queryRequest.getMessage());

        HttpEntity<Map<String, String>> askRequestEntity = new HttpEntity<>(askBody, askHeaders);

        ResponseEntity<String> askResponse = restTemplate.exchange(askUrl, HttpMethod.POST, askRequestEntity, String.class);

        Map<String, String> response = new HashMap<>();
        response.put("response", askResponse.getBody());

        return response;
    }

    private String uploadImageToImgbb(MultipartFile image) throws IOException {
        String url = "http://localhost:8000/upload-image/";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);

        Map<String, String> responseBody = response.getBody();
        return responseBody != null ? responseBody.get("image_url") : null;
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


class ImageQueryRequest {

    private String url;
    private String message;

    public ImageQueryRequest() {}

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
