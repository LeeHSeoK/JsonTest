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

    public boolean login(LoginDTO loginDTO) {
        Optional<User> result = loginRepository.findById(loginDTO.getId());
        if (result.isPresent() && result.get().getPassword().equals(loginDTO.getPassword())) { // 변경된 부분
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

    public Optional<SignUpDTO> searchOne(String id) {
        Optional<User> result = loginRepository.findById(id);
        return result.map(user -> modelMapper.map(user, SignUpDTO.class)); // 변경된 부분
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
