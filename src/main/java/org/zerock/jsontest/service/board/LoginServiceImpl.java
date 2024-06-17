package org.zerock.jsontest.service.board;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.jsontest.domain.board.User;
import org.zerock.jsontest.dto.board.LoginDTO;
import org.zerock.jsontest.dto.board.SignUpDTO;
import org.zerock.jsontest.repository.board.LoginRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class LoginServiceImpl implements LoginService {

    private final ModelMapper modelMapper;
    private final LoginRepository loginRepository;

    public boolean login(LoginDTO loginDTO){
        Optional<User> result = loginRepository.findById(loginDTO.getId());
        User user = result.orElseThrow();

        if(result.isPresent() && user.getPassword().equals(loginDTO.getPassword())){
            return true;
        }
        return false;
    }

    public boolean register(SignUpDTO signUpDTO){
        Optional<User> result = loginRepository.findById(signUpDTO.getId());
        if(result.isPresent()){
            return false;
        }
        User user = User.builder()
                .id(signUpDTO.getId())
                .name(signUpDTO.getName())
                .password(signUpDTO.getPassword())
                .build();

        loginRepository.save(user);
        return true;

    }

    public SignUpDTO searchOne(String id){
        Optional<User> result = loginRepository.findById(id);
        User user = result.orElseThrow();
        SignUpDTO signUpDTO = modelMapper.map(user, SignUpDTO.class);
        return signUpDTO;

    }

    public void modify(SignUpDTO signUpDTO){

        if(loginRepository.findById(signUpDTO.getId()).isPresent()){
            Optional<User> result = loginRepository.findById(signUpDTO.getId());
            User user = result.orElseThrow();
            user.changeUser(signUpDTO.getName(), signUpDTO.getPassword());
            loginRepository.save(user);
        }
    }
}
