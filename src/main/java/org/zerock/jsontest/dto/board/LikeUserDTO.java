package org.zerock.jsontest.dto.board;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//유저와 게시글에 대한 좋아요 클릭여부 확인 DTO
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeUserDTO {

    @NotNull
    private Long bno;

    private String id;

    private String placeName;

}