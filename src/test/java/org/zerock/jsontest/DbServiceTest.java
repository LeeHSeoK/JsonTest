package org.zerock.jsontest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.jsontest.dto.TravelDTO;
import org.zerock.jsontest.service.DbService;

@SpringBootTest
public class DbServiceTest {

    @Autowired
    private DbService dbService;

    @Test
    public void seachOneTest(){
        TravelDTO travelDTO = dbService.seachOne("광화문광장");
        String contentid = travelDTO.getContentid();
        System.out.println(contentid+"============================");
    }
}
