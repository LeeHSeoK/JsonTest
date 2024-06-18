package org.zerock.jsontest.service.board;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.zerock.jsontest.domain.board.Board;
import org.zerock.jsontest.domain.board.LikeC;
import org.zerock.jsontest.repository.board.BoardRepository;
import org.zerock.jsontest.repository.board.LikeRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    //게시글에 대한 좋아요 수 출력
    @Override
    public Long getLikeCount(Long bno) {
        Optional<LikeC> result = likeRepository.findByBoard_Bno(bno);
        LikeC like = result.orElseThrow();
        return like.getLikeCount();
    }


    //게시글 작성시 좋아요 DB 초기화
    @Override
    public void registerLike(Long bno) {
        Board board = boardRepository.findById(bno)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 번호의 게시글이 없습니다: " + bno));

        LikeC like = LikeC.builder()
                .board(board)
                .likeCount(0L)
                .build();
        likeRepository.save(like);
    }

//    @Override
//    public void setLikeCount(Long bno, Long likeCount){
//        Optional<LikeC> result = likeRepository.findByBoard_Bno(bno);
//        LikeC like = result.orElseThrow();
//        like.changeCount(likeCount);
//        likeRepository.save(like);
//    }


    //좋아요 클릭시 1증가
    @Override
    public Long incrementLikeCount(Long bno) {
        Optional<LikeC> result = likeRepository.findByBoard_Bno(bno);
        LikeC like = result.orElseThrow();
        like.changeCount(like.getLikeCount() + 1);
        likeRepository.save(like);
        return like.getLikeCount();
    }

    //좋아요 재클릭시 1감소
    @Override
    public Long decrementLikeCount(Long bno) {
        Optional<LikeC> result = likeRepository.findByBoard_Bno(bno);
        LikeC like = result.orElseThrow();
        like.changeCount(like.getLikeCount() - 1);
        likeRepository.save(like);
        return like.getLikeCount();
    }

}