package org.zerock.jsontest.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.zerock.jsontest.dto.TravelDTO;
import org.zerock.jsontest.service.DbService;
import org.zerock.jsontest.service.GptService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class GptServiceImpl implements GptService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final DbService dbService;

    public JsonNode callChatGpt(String userMsg) throws JsonProcessingException {
        final String url = "https://api.openai.com/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("model", "gpt-4o");

        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> userMessage = new HashMap<>();
        userMessage.put("role", "user");
        userMessage.put("content", userMsg);
        messages.add(userMessage);

        Map<String, String> assistantMessage = new HashMap<>();
        assistantMessage.put("role", "system");
        assistantMessage.put("content", "당신은 여행지를 추천해주는 도우미 입니다. 여행지는 하나만 추천해주세요. 예시: (질문:오늘 기분이 꿀꿀해요 여자친구랑 같이 바다를 보고싶습니다. 어디로 여행가면 좋을까요? " +
                "gpt:영일대해수욕장), (질문:오늘은 실내에서 놀고 싶습니다 저는 서울 강남에 살고있어요 어디서 놀아야될까요 차로 30분거리이내로 추천해주세요" +
                "gpt:대구수목원) 단답으로 여행지만 대답하세요");
        messages.add(assistantMessage);

        bodyMap.put("messages", messages);

        String body = objectMapper.writeValueAsString(bodyMap);

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        return objectMapper.readTree(response.getBody());
    }

    @Override
    public ResponseEntity<?> getAssistantMsg(String userMsg) throws JsonProcessingException {
        JsonNode jsonNode = callChatGpt(userMsg);
        String content = jsonNode.path("choices").get(0).path("message").path("content").asText();
        TravelDTO travelDTO = dbService.seachOne(content);
        content = travelDTO.getContentid();
        System.out.println(content+"=========================content");
        return ResponseEntity.status(HttpStatus.OK).body(content);
    }
}
//searchresultinfo?contentid=2547513
//searchresultinfo?contentId=297984