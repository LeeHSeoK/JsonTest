package org.zerock.jsontest.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Travel {
    @Id
    private String contentid;

    private String title;

    private String code1;

    private String code2;

    private String info;

    private String address1;

    private String address2;

}
