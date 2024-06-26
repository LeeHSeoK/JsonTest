package org.zerock.jsontest.service.board;

import org.zerock.jsontest.dto.board.LoginDTO;
import org.zerock.jsontest.dto.board.SignUpDTO;

import java.util.Optional;

public interface LoginService {
    boolean login(LoginDTO loginDTO);
    boolean register(SignUpDTO signUpDTO);
    void modify(SignUpDTO signUpDTO);
    Optional<SignUpDTO> searchOne(String id);
}
