package org.zerock.jsontest.service.board;

import org.zerock.jsontest.dto.board.LikeUserDTO;

public interface LikeUserService {
    void registerUserLike(LikeUserDTO likeUserDTO);
    void deleteUserLike(LikeUserDTO likeUserDTO);
    boolean searchLikeUser(LikeUserDTO likeUserDTO);
}
