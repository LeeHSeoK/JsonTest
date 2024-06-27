package org.zerock.jsontest.domain.board;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//좋아요 버튼을 사용자가 눌렀는지 기억하는 DB
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LikeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lno;
    private String userId;
    private String placeName;
    private Long bno;
}
