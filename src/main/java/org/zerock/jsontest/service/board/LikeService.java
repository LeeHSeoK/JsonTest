package org.zerock.jsontest.service.board;

import org.zerock.jsontest.dto.board.LikeDTO;

public interface LikeService {
    //게시글에 대한 좋아요 수 출력
    Long getLikeCount(Long bno);
//    void setLikeCount(Long bno, Long likeCount);
    //좋아요 클릭시 1증가
    Long incrementLikeCount(Long bno);
    //좋아요 재클릭시 1감소
    Long decrementLikeCount(Long bno);
    //게시글 작성시 좋아요 DB 초기화
    void registerLike(Long bno);
}
