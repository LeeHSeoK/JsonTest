package org.zerock.jsontest.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zerock.jsontest.domain.Travel;
import org.zerock.jsontest.dto.TravelDTO;
import org.zerock.jsontest.repository.DbRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DbServiceImpl implements DbService{

    private final ModelMapper modelMapper;
    private final DbRepository dbRepository;

    public TravelDTO seachOne(String title) {
        String sanitizedTitle = title.replaceAll("\\s", "");

        // 1. 정확히 일치하는 제목을 우선 검색
        Optional<Travel> exactResult = dbRepository.findFirstByTitle(sanitizedTitle);
        if(exactResult.isPresent()) {
            return modelMapper.map(exactResult.get(), TravelDTO.class);
        }

        // 2. 정확히 일치하는 제목이 없으면 유사한 제목 검색
        Optional<Travel> similarResult = dbRepository.findFirstByTitleContainingIgnoreCase(sanitizedTitle);
        if(similarResult.isPresent()) {
            return modelMapper.map(similarResult.get(), TravelDTO.class);
        }

        // 3. 유사한 제목도 없으면 "없음" 검색
        Optional<Travel> failResult = dbRepository.findFirstByTitleContainingIgnoreCase("없음");
        Travel travel = failResult.orElseThrow();
        TravelDTO travelDTO = modelMapper.map(travel, TravelDTO.class);
        return travelDTO;
    }
}
