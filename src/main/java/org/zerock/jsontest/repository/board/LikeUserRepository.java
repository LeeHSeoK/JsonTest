package org.zerock.jsontest.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.jsontest.domain.board.LikeUser;

import java.util.Optional;

public interface LikeUserRepository extends JpaRepository<LikeUser, String> {
    // userid와 bno에 맞는 LikeUser 엔터티를 찾기 위한 메서드
    Optional<LikeUser> findByUserIdAndBno(String userId, Long bno);
    boolean existsByUserIdAndBno(String userId, Long bno);

    // userid와 bno에 맞는 LikeUser 엔터티를 삭제하기 위한 메서드
    void deleteByUserIdAndBno(String userId, Long bno);
}
