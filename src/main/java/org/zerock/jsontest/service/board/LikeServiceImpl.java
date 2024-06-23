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

    private final BoardRepository boardRepository;

    // 게시글에 대한 좋아요 수 출력
    @Override
    public Long getLikeCount(Long bno) {
        Board board = boardRepository.findById(bno).orElseThrow();
        return board.getLikeCount();
    }

    // 좋아요 클릭 시 1 증가
    @Override
    public Long incrementLikeCount(Long bno) {
        Board board = boardRepository.findById(bno).orElseThrow();
        board.changeLikeCount(board.getLikeCount() + 1);
        boardRepository.save(board);
        return board.getLikeCount();
    }

    // 좋아요 재클릭 시 1 감소
    @Override
    public Long decrementLikeCount(Long bno) {
        Board board = boardRepository.findById(bno).orElseThrow();
        board.changeLikeCount(board.getLikeCount() - 1);
        boardRepository.save(board);
        return board.getLikeCount();
    }
}