package org.zerock.jsontest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.jsontest.service.board.LikeService;

@SpringBootTest
public class LikeCountTest {

    @Autowired
    private LikeService likeService;

}
