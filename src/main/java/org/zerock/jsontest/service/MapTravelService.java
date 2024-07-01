package org.zerock.jsontest.service;

import org.zerock.jsontest.dto.MapDTO;
import org.zerock.jsontest.dto.SigunguDTO;

import java.io.UnsupportedEncodingException;

public interface MapTravelService {
    MapDTO getMapTravel(String areaCode, String sigunguCode, int pageNo, int numOfRows) throws UnsupportedEncodingException;
    SigunguDTO getSigungu(String areaCode) throws UnsupportedEncodingException;
    MapDTO searchByKeyword(String keyword, int pageNo, int numOfRows);
}
