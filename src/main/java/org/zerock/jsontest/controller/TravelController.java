package org.zerock.jsontest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import com.fasterxml.jackson.databind.ObjectMapper; // Jackson 라이브러리 추가 필요
import org.zerock.jsontest.DTO.ReadOneDTO;
import org.zerock.jsontest.DTO.SearchListDTO;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
public class TravelController {

    private static final String SERVICE_KEY = ""; // Replace with your actual service key
    SearchListDTO response1;
    ReadOneDTO response2;

    @GetMapping("/search")
    public String searchKeyword(@RequestParam(value = "keyword", required = false, defaultValue = "대구") String keyword,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                @RequestParam(value = "numOfRows", required = false, defaultValue = "10") int numOfRows,
                                Model model) {
        try {
            String encodedServiceKey = URLEncoder.encode(SERVICE_KEY, "UTF-8");
            String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
            String uri = "http://apis.data.go.kr/B551011/KorService1/searchKeyword1?serviceKey=" + encodedServiceKey +
                    "&numOfRows=" + numOfRows + "&pageNo=" + page + "&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A&keyword=" + encodedKeyword + "&contentTypeId=12";

            RestTemplate restTemplate = new RestTemplate();
            URI url = new URI(uri);
            String jsonResult = restTemplate.getForObject(url, String.class);

            response1 = new ObjectMapper().readValue(jsonResult, SearchListDTO.class);

            model.addAttribute("items", response1.getResponse().getBody().getItems().getItem());
            model.addAttribute("keyword", keyword);
            model.addAttribute("currentPage", page);
            model.addAttribute("numOfRows", numOfRows);
            model.addAttribute("totalCount", response1.getResponse().getBody().getTotalCount());

        } catch (HttpClientErrorException e) {
            model.addAttribute("error", "API key is invalid or not registered.");
        } catch (URISyntaxException e) {
            model.addAttribute("error", "URI Syntax Exception: " + e.getMessage());
        } catch (UnsupportedEncodingException e) {
            model.addAttribute("error", "Encoding Exception: " + e.getMessage());
        } catch (Exception e) {
            model.addAttribute("error", "An unexpected error occurred: " + e.getMessage());
        }
        return "searchresult";
    }

    @GetMapping("/searchresultinfo")
    public String searchContentId(@RequestParam(value = "contentId") String contentId, Model model) {
        try {
            String encodedServiceKey = URLEncoder.encode(SERVICE_KEY, "UTF-8");
            String encodedcontentid = URLEncoder.encode(contentId, "UTF-8");

            String uri = "https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=" + encodedServiceKey +
                    "&MobileOS=ETC&MobileApp=AppTest&_type=json&contentId=" + encodedcontentid + "&contentTypeId=12" +
                    "&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&numOfRows=10&pageNo=1";

            RestTemplate restTemplate = new RestTemplate();
            URI url = new URI(uri);
            String jsonResult1 = restTemplate.getForObject(url, String.class);
            System.out.println(jsonResult1+"-------------------------------");

            response2 = new ObjectMapper().readValue(jsonResult1, ReadOneDTO.class);
            System.out.println(response2+"===================================");

            model.addAttribute("item", response2.getResponse().getBody().getItems().getItem()[0]);

        } catch (Exception e) {

            e.printStackTrace();
        }
        return "searchresultinfo";
    }
}
