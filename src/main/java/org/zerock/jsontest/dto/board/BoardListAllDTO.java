package org.zerock.jsontest.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardListAllDTO {
    private Long bno;
    private String title;
    private String content;
    private String name;
    private LocalDateTime regDate;
    private Long replyCount;
    private List<BoardImageDTO> boardImages;
    //좋아요 카운트값 추가
    private Long likeCount;
    private Long viewCount;
}
