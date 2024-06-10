package org.zerock.jsontest.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Body {
    private Items items;
    private int totalCount; // totalCount 필드 추가
}