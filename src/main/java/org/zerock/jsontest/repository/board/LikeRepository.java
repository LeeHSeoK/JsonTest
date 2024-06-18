package org.zerock.jsontest.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.jsontest.domain.board.LikeC;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeC, Long> {
    Optional<LikeC> findByBoard_Bno(Long bno);
}
