package org.zerock.jsontest.service.dbService;

import org.zerock.jsontest.dto.TravelDTO;

public interface DbService {

    TravelDTO searchOne(String title);

}
