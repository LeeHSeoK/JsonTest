package org.zerock.jsontest.dto.board;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListReplyCountDTO {
    private Long bno;
    private String title;
    private String name;
    private LocalDateTime regDate;
    private Long replyCount;
}
