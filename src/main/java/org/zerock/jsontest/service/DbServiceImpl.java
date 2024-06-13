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
        Optional<Travel> result = dbRepository.findByTitle(title);
        Travel travel = result.orElseThrow();
        TravelDTO travelDTO = modelMapper.map(travel, TravelDTO.class);
        return travelDTO;
    }
}
