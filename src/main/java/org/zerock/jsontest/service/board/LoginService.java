package org.zerock.jsontest.service.board;

import org.zerock.jsontest.dto.board.LoginDTO;
import org.zerock.jsontest.dto.board.SignUpDTO;

public interface LoginService {
    boolean login(LoginDTO loginDTO);
    boolean register(SignUpDTO signUpDTO);
    void modify(SignUpDTO signUpDTO);
    SignUpDTO searchOne(String id);
}
