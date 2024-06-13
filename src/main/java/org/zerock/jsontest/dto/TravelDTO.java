package org.zerock.jsontest.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TravelDTO {
    private String contentid;
    private String title;
    private String code1;
}
