package org.zerock.jsontest.service.board;

import org.zerock.jsontest.dto.board.PageRequestDTO;
import org.zerock.jsontest.dto.board.PageResponseDTO;
import org.zerock.jsontest.dto.board.ReplyDTO;

public interface ReplyService {
    Long register(ReplyDTO replyDTO);
    ReplyDTO readOne(Long rno);
    void modify(ReplyDTO replyDTO);
    void remove(Long rno);

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);
}
