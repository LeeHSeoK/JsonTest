package org.zerock.jsontest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.jsontest.domain.board.LikeC;
import org.zerock.jsontest.repository.board.LikeRepository;

import java.util.stream.IntStream;

@SpringBootTest
public class LikeCountTest {

    @Autowired
    private LikeRepository likeRepository;


}
