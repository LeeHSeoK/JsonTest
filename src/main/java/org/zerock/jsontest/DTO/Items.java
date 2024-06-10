package org.zerock.jsontest.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Items {  // 접근 제어자를 public으로 변경
    private Item[] item;
}