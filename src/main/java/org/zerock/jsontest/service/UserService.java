package org.zerock.jsontest.service;

import org.zerock.jsontest.dto.UserDTO;

public interface UserService {
    String signup(UserDTO userDTO);
    UserDTO login(String id, String password);
}
