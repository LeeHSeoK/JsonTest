package org.zerock.jsontest.service.dbService;

import org.zerock.jsontest.dto.TravelDTO;

public interface DbService {
    //OpenAI를 통해 받아온 값을 가지고 DB에서 찾아서 출력
    TravelDTO searchOne(String title);

}
