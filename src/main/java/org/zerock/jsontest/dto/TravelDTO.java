package org.zerock.jsontest.dto;

import lombok.*;


// 공공기관 API 중 필요한 부분만 가져온 DTO
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TravelDTO {
    private String contentid;
    private String title;
    private String code1;
    private String contenttypeid;
    private String info;
    private String address1;
    private String address2;
}
