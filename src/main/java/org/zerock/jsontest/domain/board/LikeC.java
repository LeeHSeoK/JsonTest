package org.zerock.jsontest.domain.board;

import jakarta.persistence.*;
import lombok.*;


//게시글에 대한 좋아요 카운트 DB
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class LikeC {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // LikeC 엔티티의 기본 키

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bno") // JoinColumn을 통해 외래 키를 지정합니다.
    private Board board; // LikeC 엔티티에서 Board 엔티티를 참조합니다.

    @Column(nullable = false)
    @Builder.Default
    private Long likeCount = 0L;

    // 좋아요 수 변경 메서드
    public void changeCount(Long likeCount) {
        this.likeCount = likeCount;
    }
}
