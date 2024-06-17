package org.zerock.jsontest.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.Banner;
import org.springframework.cache.support.NullValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import com.fasterxml.jackson.databind.ObjectMapper; // Jackson 라이브러리 추가 필요
import org.zerock.jsontest.dto.ReadOneDTO;
import org.zerock.jsontest.dto.SearchListDTO;
import org.zerock.jsontest.dto.TravelDTO;

import java.net.URI;
import java.net.URISyntaxException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Controller
public class TravelController {

    @Value("${service_key}")
    private String SERVICE_KEY; // Replace with your actual service key
    SearchListDTO response1;
    ReadOneDTO response2;
    Random random = new Random();


    @GetMapping("/home")
    public String home(@RequestParam(value = "keyword", required = false, defaultValue = "대구") String keyword,
//                                @RequestParam(value = "page", required = false, defaultValue = "") int page,
                                @RequestParam(value = "numOfRows", required = false, defaultValue = "20") int numOfRows,
                                Model model, HttpServletRequest req) {
        int page = random.nextInt(1)+1;
        try {
            String encodedServiceKey = URLEncoder.encode(SERVICE_KEY, "UTF-8");
            String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
            String uri = "http://apis.data.go.kr/B551011/KorService1/searchKeyword1?serviceKey=" + encodedServiceKey +
                    "&numOfRows=" + numOfRows + "&pageNo=" + page + "&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A&keyword=" + encodedKeyword + "&contentTypeId=12";

            RestTemplate restTemplate = new RestTemplate();
            URI url = new URI(uri);
            String jsonResult = restTemplate.getForObject(url, String.class);

            response1 = new ObjectMapper().readValue(jsonResult, SearchListDTO.class);

            //검증 데이터 추가 06-14
            // 리스트 받아서 뿌려주고 ?
            SearchListDTO.Response.Body.Items.Item[] items = response1.getResponse().getBody().getItems().getItem();
            List<String> imglist = new ArrayList<>();
            List<String> contentlist = new ArrayList<>();
            System.out.println("이거실행?");
            for(int i=0; i<items.length;i++){
                String img = items[i].getFirstimage();
                String contentid = items[i].getContentid();
                if(img.isEmpty()){
                    continue;
                }

                imglist.add(img);
                contentlist.add(contentid);
            }

            if (imglist.isEmpty() || contentlist.isEmpty()) {
                // 만약 필요한 데이터가 null이거나 비어있다면 다시 데이터를 받아옴
                return "redirect:/home?keyword=" + URLEncoder.encode(keyword, "UTF-8") + "&numOfRows=" + numOfRows;
            }

//            model.addAttribute("items", response1.getResponse().getBody().getItems().getItem());
            //이미지 리스트 추가'
            model.addAttribute("img",imglist);
            model.addAttribute("content", contentlist);
            req.setAttribute("notice", imglist);
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
        return "home";
    }

    @GetMapping("/search")
    public String searchKeyword(@RequestParam(value = "keyword", required = false, defaultValue = "대구") String keyword,
//                                @RequestParam(value = "page", required = false, defaultValue = "") int page,
                                @RequestParam(value = "numOfRows", required = false, defaultValue = "20") int numOfRows,
                                Model model, HttpServletRequest req) throws UnsupportedEncodingException {
        int page = random.nextInt(1)+1;
        try {
            String encodedServiceKey = URLEncoder.encode(SERVICE_KEY, "UTF-8");
            String encodedKeyword = URLEncoder.encode(keyword, "UTF-8");
            String uri = "http://apis.data.go.kr/B551011/KorService1/searchKeyword1?serviceKey=" + encodedServiceKey +
                    "&numOfRows=" + numOfRows + "&pageNo=" + page + "&MobileOS=ETC&MobileApp=AppTest&_type=json&listYN=Y&arrange=A&keyword=" + encodedKeyword + "&contentTypeId=12";

            RestTemplate restTemplate = new RestTemplate();
            URI url = new URI(uri);
            String jsonResult = restTemplate.getForObject(url, String.class);

            response1 = new ObjectMapper().readValue(jsonResult, SearchListDTO.class);

            //검증 데이터 추가 06-14
            // 리스트 받아서 뿌려주고 ?
            SearchListDTO.Response.Body.Items.Item[] items = response1.getResponse().getBody().getItems().getItem();
            List<String> imglist = new ArrayList<>();
            List<String> contentlist = new ArrayList<>();
            System.out.println("이거실행?");
            for(int i=0; i<items.length;i++){
                String img = items[i].getFirstimage();
                String contentid = items[i].getContentid();
                if(img.isEmpty()){
                    continue;
                }

                imglist.add(img);
                contentlist.add(contentid);
            }

            model.addAttribute("img",imglist);
            model.addAttribute("content", contentlist);
            model.addAttribute("notice", imglist);
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
            return "redirect:/home?keyword=" + URLEncoder.encode(keyword, "UTF-8") + "&numOfRows=" + numOfRows;
        }
        return "home";
    }

    @GetMapping("/searchresultinfo")
    public String searchcontentId(@RequestParam(value = "contentId") String contentId, Model model) {
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
