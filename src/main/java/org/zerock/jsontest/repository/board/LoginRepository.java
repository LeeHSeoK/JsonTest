package org.zerock.jsontest.repository.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.jsontest.domain.board.User;

public interface LoginRepository extends JpaRepository<User, String> {
}
