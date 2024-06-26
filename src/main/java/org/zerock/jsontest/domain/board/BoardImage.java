package org.zerock.jsontest.domain.board;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "board")
public class BoardImage implements Comparable<BoardImage>{

    @Id
    private String uuid;

    private String fileName;
    private int ord;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @Override
    public int compareTo(BoardImage other){
        return this.ord - other.ord;    //순서 확인용
    }

    public void changeBoard(Board board){
        this.board = board;
    }
}
