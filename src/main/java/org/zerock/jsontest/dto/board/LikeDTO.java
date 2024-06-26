package org.zerock.jsontest.dto.board;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//좋아요 카운트를 받아오는 DTO
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeDTO {

    @NotNull
    private Long bno;

    private Long likeCount;

}
