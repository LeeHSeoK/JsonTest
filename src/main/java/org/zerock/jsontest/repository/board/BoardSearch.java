package org.zerock.jsontest.repository.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.jsontest.domain.board.Board;
import org.zerock.jsontest.dto.board.BoardListAllDTO;
import org.zerock.jsontest.dto.board.BoardListReplyCountDTO;

public interface BoardSearch {
//    Page<Board> search1(Pageable pageable);
//    Page<Board> searchAll(String[] types, String keyword, Pageable pageable);
//
//    Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable);

    Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable);
}
