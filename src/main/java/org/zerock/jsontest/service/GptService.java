package org.zerock.jsontest.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface GptService {
    ResponseEntity<?> getAssistantMsg(String userMsg) throws JsonProcessingException;
}
