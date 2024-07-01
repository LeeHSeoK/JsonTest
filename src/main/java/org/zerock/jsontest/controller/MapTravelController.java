package org.zerock.jsontest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.zerock.jsontest.dto.MapDTO;
import org.zerock.jsontest.dto.ReadOneDTO;
import org.zerock.jsontest.dto.SigunguDTO;
import org.zerock.jsontest.service.MapTravelService;

import java.net.URI;
import java.net.URLEncoder;

@Controller
public class MapTravelController {

    private static final Logger logger = LoggerFactory.getLogger(MapTravelController.class);

    @Autowired
    private MapTravelService mapTravelService;

    @Value("${service_key}")
    private String SERVICE_KEY;

    @GetMapping("/travelmap/main")
    public String main() {
        return "travelmap/main"; // travelmap/main.html 파일을 반환
    }

    @GetMapping("/getMapTravel")
    @ResponseBody
    public MapDTO getMapTravel(@RequestParam("areaCode") String areaCode,
                               @RequestParam(value = "sigunguCode", required = false) String sigunguCode,
                               @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                               @RequestParam(value = "numOfRows", defaultValue = "10") int numOfRows) {
        try {
            return mapTravelService.getMapTravel(areaCode, sigunguCode, pageNo, numOfRows);
        } catch (Exception e) {
            logger.error("Error retrieving map travel data", e);
            throw new RuntimeException("Error retrieving map travel data", e);
        }
    }

    @GetMapping("/getSigungu")
    @ResponseBody
    public SigunguDTO getSigungu(@RequestParam("areaCode") String areaCode) {
        try {
            return mapTravelService.getSigungu(areaCode);
        } catch (Exception e) {
            logger.error("Error retrieving sigungu data", e);
            throw new RuntimeException("Error retrieving sigungu data", e);
        }
    }

    @GetMapping("/searchByKeyword")
    @ResponseBody
    public MapDTO searchByKeyword(@RequestParam("keyword") String keyword,
                                  @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                  @RequestParam(value = "numOfRows", defaultValue = "10") int numOfRows) {
        try {
            return mapTravelService.searchByKeyword(keyword, pageNo, numOfRows);
        } catch (Exception e) {
            logger.error("Error retrieving keyword search data", e);
            throw new RuntimeException("Error retrieving keyword search data", e);
        }
    }

    @GetMapping("map/searchresultinfo")
    public String searchcontentId(@RequestParam(value = "contentId") String contentId,
                                  @RequestParam(value = "contentTypeId") String contentTypeId, Model model) {
        try {

            String encodedServiceKey = URLEncoder.encode(SERVICE_KEY, "UTF-8");
            String encodedcontentid = URLEncoder.encode(contentId, "UTF-8");
            String encodedcontenttypeid = URLEncoder.encode(contentTypeId, "UTF-8");

            String uri = "https://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=" + encodedServiceKey +
                    "&MobileOS=ETC&MobileApp=AppTest&_type=json&contentId=" + encodedcontentid + "&contentTypeId=" + encodedcontenttypeid +
                    "&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y&numOfRows=10&pageNo=1";

            RestTemplate restTemplate = new RestTemplate();
            URI url = new URI(uri);
            String jsonResult1 = restTemplate.getForObject(url, String.class);
            System.out.println(jsonResult1 + "-------------------------------");

            ReadOneDTO response2 = new ObjectMapper().readValue(jsonResult1, ReadOneDTO.class);
            System.out.println(response2 + "===================================");

            model.addAttribute("item", response2.getResponse().getBody().getItems().getItem()[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "searchresultinfo";
    }
}
