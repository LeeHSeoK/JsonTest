package org.zerock.jsontest.repository.board;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.jsontest.domain.*;
import org.zerock.jsontest.domain.board.Board;
import org.zerock.jsontest.domain.board.QBoard;
import org.zerock.jsontest.domain.board.QReply;
import org.zerock.jsontest.dto.board.BoardImageDTO;
import org.zerock.jsontest.dto.board.BoardListAllDTO;
import org.zerock.jsontest.dto.board.BoardListReplyCountDTO;

import java.util.List;
import java.util.stream.Collectors;

public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {
    public BoardSearchImpl() {
        super(Board.class);
    }

//    @Override
//    public Page<Board> search1(Pageable pageable){
//        QBoard board = QBoard.board;
//        JPQLQuery<Board> query = from(board);
//        query.where(board.title.contains("3"));
//
//        this.getQuerydsl().applyPagination(pageable, query);
//
//        List<Board> list = query.fetch();
//        long count = query.fetchCount();
//
//        //페이징을 위한 page객체
//        return new PageImpl<>(list, pageable, count);
//        //return null;
//    }

//    @Override
//    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
//        QBoard board = QBoard.board;
//        JPQLQuery<Board> query = from(board);
//
//        if ((types != null && types.length > 0) && keyword != null) {
//            BooleanBuilder booleanBuilder = new BooleanBuilder();
//            for (String type : types) {
//                switch (type) {
//                    case "t":
//                        booleanBuilder.or(board.title.contains(keyword));
//                        break;
//                    case "c":
//                        booleanBuilder.or(board.content.contains(keyword));
//                        break;
//                    case "w":
//                        booleanBuilder.or(board.name.contains(keyword));
//                        break;
//                }
//            }
//            query.where(booleanBuilder);
//        }
//        query.where(board.bno.gt(0L));
//
//        //paging
//        this.getQuerydsl().applyPagination(pageable, query);
//        List<Board> list = query.fetch();
//        Long count = query.fetchCount();
//        return new PageImpl<>(list, pageable, count);
//    }
//
//    @Override
//    public Page<BoardListReplyCountDTO> searchWithReplyCount(String[] types, String keyword, Pageable pageable) {
//        QBoard board = QBoard.board;
//        QReply reply = QReply.reply;
//
//        JPQLQuery<Board> query = from(board);   //board에서 select해온다.
//        query.leftJoin(reply).on(reply.board.eq(board));
//        query.groupBy(board);
//
//        if ((types != null && types.length > 0) && keyword != null) {
//            BooleanBuilder booleanBuilder = new BooleanBuilder();
//            for (String type : types) {
//                switch (type) {
//                    case "t":
//                        booleanBuilder.or(board.title.contains(keyword));
//                        break;
//                    case "c":
//                        booleanBuilder.or(board.content.contains(keyword));
//                        break;
//                    case "w":
//                        booleanBuilder.or(board.name.contains(keyword));
//                        break;
//                }
//            }
//            query.where(booleanBuilder);
//        }
//        query.where(board.bno.gt(0L));
//
//        JPQLQuery<BoardListReplyCountDTO> dtoQuery = query.select(Projections.
//                bean(BoardListReplyCountDTO.class,
//                        board.bno,
//                        board.title,
//                        board.name,
//                        board.regDate,
//                        reply.count().as("replyCount")));
//
//        this.getQuerydsl().applyPagination(pageable, dtoQuery);
//        List<BoardListReplyCountDTO> dtolist = dtoQuery.fetch();
//        Long count = dtoQuery.fetchCount();
//        return new PageImpl<>(dtolist, pageable, count);
//    }

    @Override
    public Page<BoardListAllDTO> searchWithAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        QReply reply = QReply.reply;

        JPQLQuery<Board> query = from(board);   //board에서 select해온다.
        query.leftJoin(reply).on(reply.board.eq(board));
        query.groupBy(board);

        if ((types != null && types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.name.contains(keyword));
                        break;
                }
            }
            query.where(booleanBuilder);
        }
//        query.where(board.bno.gt(0L));
//        query.groupBy(board);


        getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleJPQLQuery = query.select(board, reply.countDistinct());

        List<Tuple> tupleList = tupleJPQLQuery.fetch();

        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
            Board board1 = (Board) tuple.get(board);
            long replyCount = tuple.get(1, Long.class);
            BoardListAllDTO dto = BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .name(board1.getName())
                    .regDate(board1.getRegDate())
                    .replyCount(replyCount)
                    .viewCount(board1.getViewCount())
                    .content(board1.getContent())
                    .viewCount(board1.getViewCount())
                    .build();
            //BoardImage를 BoardImageDTO로 처리
            List<BoardImageDTO> imageDTOS = board1.getImageSet().stream().sorted()
                    .map(boardImage -> BoardImageDTO.builder().uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .ord(boardImage.getOrd())
                            .build())
                    .collect(Collectors.toList());
            dto.setBoardImages(imageDTOS);
            return dto;

        }).collect(Collectors.toList());

        long totalCount = query.fetchCount();

        return new PageImpl<>(dtoList, pageable, totalCount);

    }

}
