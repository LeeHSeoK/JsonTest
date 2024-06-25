package org.zerock.jsontest.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RecommendationService {

    public String[] getRecommendations(String userid) {

        String url = "http://localhost:8000/recommend?userid=" + userid;
        RestTemplate restTemplate = new RestTemplate();
        String[] recommendations = restTemplate.getForObject(url, String[].class);
        return recommendations;
    }
}
