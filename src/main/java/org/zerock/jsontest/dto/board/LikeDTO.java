package org.zerock.jsontest.dto.board;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeDTO {

    @NotNull
    private Long bno;

    private Long likeCount;

}
