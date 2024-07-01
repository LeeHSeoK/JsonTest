package org.zerock.jsontest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.zerock.jsontest.dto.MapDTO;
import org.zerock.jsontest.dto.SigunguDTO;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

@Service
public class MapTravelServiceImpl implements MapTravelService {

    private static final Logger logger = LoggerFactory.getLogger(MapTravelServiceImpl.class);

    @Value("${service_key}")
    private String SERVICE_KEY;

    @Override
    public MapDTO getMapTravel(String areaCode, String sigunguCode, int pageNo, int numOfRows) {
        try {
            String encodedAreaCode = URLEncoder.encode(areaCode, "UTF-8");
            String encodedServiceKey = URLEncoder.encode(SERVICE_KEY, "UTF-8");

            String uri = "https://apis.data.go.kr/B551011/KorService1/areaBasedList1?serviceKey=" + encodedServiceKey +
                    "&numOfRows=" + numOfRows + "&pageNo=" + pageNo +
                    "&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y" +
                    "&arrange=O&contentTypeId=12&areaCode=" + encodedAreaCode;
            if (sigunguCode != null && !sigunguCode.isEmpty()) {
                String encodedSigunguCode = URLEncoder.encode(sigunguCode, "UTF-8");
                uri += "&sigunguCode=" + encodedSigunguCode;
            }

            RestTemplate restTemplate = new RestTemplate();
            URI url = new URI(uri);
            String result = restTemplate.getForObject(url, String.class);

            // JSON 문자열을 Java 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(result, MapDTO.class);
        } catch (UnsupportedEncodingException | URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException("JSON parsing error", e);
        }
    }

    @Override
    public SigunguDTO getSigungu(String areaCode) throws UnsupportedEncodingException {
        try {
            String encodedAreaCode = URLEncoder.encode(areaCode, "UTF-8");
            String encodedServiceKey = URLEncoder.encode(SERVICE_KEY, "UTF-8");

            String uri = "https://apis.data.go.kr/B551011/KorService1/areaCode1?serviceKey=" + encodedServiceKey +
                    "&numOfRows=25&pageNo=1&MobileOS=ETC&MobileApp=AppTest&_type=json&areaCode=" + encodedAreaCode;
            logger.info("Requesting sigungu data with URI: {}", uri);  // 로그 추가
            RestTemplate restTemplate = new RestTemplate();
            URI url = new URI(uri);
            String result = restTemplate.getForObject(url, String.class);

            // JSON 문자열을 Java 객체로 변환
            logger.info("Sigungu data response: {}", result);  // 로그 추가
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(result, SigunguDTO.class);
        } catch (UnsupportedEncodingException | URISyntaxException e) {
            logger.error("Encoding/URI error", e);  // 로그 추가
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("JSON parsing error", e);  // 로그 추가
            throw new RuntimeException("JSON parsing error", e);
        }
    }

    @Override
    public MapDTO searchByKeyword(String keyword, int pageNo, int numOfRows) {
        try {
            String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
            String encodedServiceKey = URLEncoder.encode(SERVICE_KEY, "UTF-8");

            String uri = "https://apis.data.go.kr/B551011/KorService1/searchKeyword1?serviceKey=" + encodedServiceKey +
                    "&numOfRows=" + numOfRows + "&pageNo=" + pageNo +
                    "&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y" +
                    "&arrange=O&keyword=" + encodedKeyword + "&contentTypeId=12";

            RestTemplate restTemplate = new RestTemplate();
            URI url = new URI(uri);
            String result = restTemplate.getForObject(url, String.class);

            // JSON 문자열을 Java 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(result, MapDTO.class);
        } catch (UnsupportedEncodingException | URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException("JSON parsing error", e);
        }
    }
}
