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
    private String contenttypeid;
    private String info;
    private String address1;
    private String address2;
}
