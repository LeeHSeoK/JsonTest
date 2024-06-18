package org.zerock.jsontest.dto.board;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
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
public class BoardDTO {
    private long bno;


    @NotEmpty
    @Size(min = 3, max = 10)
    private String title;

    @NotEmpty
    private String id;

    @NotEmpty
    private String content;
    @NotEmpty
    private String name;
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    //첨부파일들의 이름
    private List<String> fileNames;

    private String xaixs;

    private String yaixs;

    private String placeName;
}
