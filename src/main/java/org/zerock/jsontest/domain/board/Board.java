package org.zerock.jsontest.domain.board;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.zerock.jsontest.domain.BaseEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"imageSet","replies"})
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String id;
    @Column(length=500, nullable=false)
    private String title;
    @Column(length=2000, nullable=false)
    private String content;
    @Column(length=500, nullable=false)
    private String name;
    @Column(length=50)
    private String xaxis;
    @Column(length=50)
    private String yaxis;
    @Column(length=50)
    private String placeName;
    @Column(nullable = false)
    private Long viewCount = 0L; // 조회수 컬럼 기본값 설정

    @Column(nullable = false)
    private Long likeCount = 0L; // 좋아요 수 컬럼 기본값 설정

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @BatchSize(size=20)
    private Set<BoardImage> imageSet = new HashSet<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @BatchSize(size=20)
    private List<Reply> replies;

    public void change(String title, String content, String name, String xaxis, String yaxis, String placeName){
        this.title = title;
        this.content = content;
        this.name = name;
        this.xaxis = xaxis;
        this.yaxis = yaxis;
        this.placeName = placeName;
    }

    public void addImage(String uuid, String FileName){
        BoardImage image = BoardImage.builder()
                .uuid(uuid)
                .fileName(FileName)
                .board(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(image);
    }

    public void clearImage(){
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));
        this.imageSet.clear();
    }

    // 좋아요 수 변경 메서드
    public void changeLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }
}
