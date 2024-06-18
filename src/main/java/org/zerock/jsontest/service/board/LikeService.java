package org.zerock.jsontest.service.board;

import org.zerock.jsontest.dto.board.LikeDTO;

public interface LikeService {
    Long getLikeCount(Long bno);
//    void setLikeCount(Long bno, Long likeCount);
    Long incrementLikeCount(Long bno);
    Long decrementLikeCount(Long bno);
    void registerLike(Long bno);
}
