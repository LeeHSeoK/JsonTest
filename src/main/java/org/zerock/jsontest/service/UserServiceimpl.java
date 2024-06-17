package org.zerock.jsontest.service;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zerock.jsontest.domain.User;
import org.zerock.jsontest.dto.UserDTO;
import org.zerock.jsontest.repository.UserRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceimpl implements UserService{
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public String signup(UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);
        return userRepository.save(user).getId();
    }

    @Override
    public UserDTO login(String id, String password){
        Optional<User> result = userRepository.findByIdAndPassword(id, password);

        User user = result.orElseThrow(() -> new NoSuchElementException("Invalid username or password"));
        //받아온걸 다시 userDTO로 바꿔준다.?
        return modelMapper.map(user, UserDTO.class);
    }
}
