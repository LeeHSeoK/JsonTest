package org.zerock.jsontest.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.jsontest.domain.board.LikeUser;

public interface LikeUserRepository extends JpaRepository<LikeUser, String> {
}
