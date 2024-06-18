package org.zerock.jsontest.service.board;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zerock.jsontest.domain.board.LikeUser;
import org.zerock.jsontest.dto.board.LikeUserDTO;
import org.zerock.jsontest.repository.board.LikeUserRepository;

@Service
@RequiredArgsConstructor
public class LikeUserServiceImpl implements LikeUserService {

    private final LikeUserRepository likeUserRepository;
    private final ModelMapper modelMapper;

    @Override
    public void registerUserLike(LikeUserDTO likeUserDTO){
        LikeUser likeUser = modelMapper.map(likeUserDTO, LikeUser.class);
        likeUserRepository.save(likeUser);
    }

    @Override
    public void deleteUserLike(LikeUserDTO likeUserDTO){
        LikeUser likeUser = modelMapper.map(likeUserDTO, LikeUser.class);
        likeUserRepository.save(likeUser);
    }
}
