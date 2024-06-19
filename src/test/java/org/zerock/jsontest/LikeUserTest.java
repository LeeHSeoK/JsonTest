package org.zerock.jsontest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.jsontest.domain.board.LikeUser;
import org.zerock.jsontest.dto.board.LikeUserDTO;
import org.zerock.jsontest.repository.board.LikeUserRepository;
import org.zerock.jsontest.service.board.LikeUserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class LikeUserTest {

    @Autowired
    LikeUserRepository likeUserRepository;
    @Autowired
    LikeUserService likeUserService;

    @Test
    @Transactional
    public void testDeleteByIdAndBno() {
        String id  = "user1";
        Long bno = 134L;
//        likeUserRepository.deleteByUserIdAndBno(userid, bno);

//        Optional<LikeUser> result = likeUserRepository.findByIdAndBno(id, bno);
//        assertTrue(result.isEmpty(), "LikeUser 엔티티가 삭제되지 않았습니다.");

    }

    @Test
    public void testregister() {
        String id  = "user1";
        Long bno = 135L;
        LikeUserDTO dto = new LikeUserDTO();
        dto.setBno(bno);
        dto.setId(id);
        likeUserService.registerUserLike(dto);
    }

}
