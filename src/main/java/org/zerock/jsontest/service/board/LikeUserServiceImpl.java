package org.zerock.jsontest.service.board;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public void deleteUserLike(LikeUserDTO likeUserDTO){
        String userId = likeUserDTO.getId();
        Long bno = likeUserDTO.getBno();
        // Repository의 deleteByUserIdAndBno 메서드를 사용하여 삭제
        likeUserRepository.deleteByUserIdAndBno(userId, bno);
    }

    @Override
    public boolean searchLikeUser(LikeUserDTO likeUserDTO){
        String userId = likeUserDTO.getId();
        Long bno = likeUserDTO.getBno();
        return likeUserRepository.existsByUserIdAndBno(userId, bno);
    }
}
