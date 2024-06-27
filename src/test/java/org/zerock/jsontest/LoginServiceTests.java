package org.zerock.jsontest;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.jsontest.dto.board.SignUpDTO;
import org.zerock.jsontest.service.board.LoginService;

import java.util.stream.LongStream;

@SpringBootTest
@Log4j2
public class LoginServiceTests {

    @Autowired
    private LoginService loginService;

    @Test
    public void signup() {
        LongStream.rangeClosed(0,100L).forEach(i->{
          SignUpDTO signUpDTO = SignUpDTO.builder()
                .id("user"+i)
                .name("name"+i)
                .password("12341234")
                .build();
        loginService.register(signUpDTO);
        });
    }
}
