package org.zerock.jsontest;

import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.zerock.jsontest.domain.board.Board;
import org.zerock.jsontest.domain.board.BoardImage;
import org.zerock.jsontest.domain.board.LikeC;
import org.zerock.jsontest.dto.board.BoardListReplyCountDTO;
import org.zerock.jsontest.repository.board.BoardRepository;
import org.zerock.jsontest.repository.board.LikeRepository;
import org.zerock.jsontest.repository.board.ReplyRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardRepositroyTests {
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Test
    public void testInsert() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            Board board = Board.builder()
                    .title("Title......" + i%10)
                    .content("Content....." + i%10)
                    .name("name" + i%10)
                    .id("user" + i%10)
                    .viewCount(0L)
                    .build();

            boardRepository.save(board);

            LikeC likeC = LikeC.builder()
                    .likeCount(0L)
                    .board(board)
                    .build();

            likeRepository.save(likeC);
        });
    }

    @Test
    public void testSelect() {
        Long bno = 10L;                         //findByID = select query문의 역할을 할듯?
        Optional<Board> result = boardRepository.findById(bno); //Optional 값이 있으면<>으로 가져오고 없으면 null일떄 오류가 발생하지 않음
//        result.ifPresent(board -> System.out.println(board));
        Board board = result.orElseThrow();
        log.info(board);
    }

//    @Test
//    public void testUpdate(){
//        Long bno = 10L;
//        Optional<Board> result = boardRepository.findById(bno);
//        Board board = result.orElseThrow();
//        board.change("update...title","update...content","update..name");
//        boardRepository.save(board);
//    }

    @Test
    public void testDelete(){
        Long bno = 9L;
        boardRepository.deleteById(bno);
    }

    @Test
    public void testPaging(){
        //1 page. order by bno desc
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count: "+result.getTotalElements());
        log.info("total pages: "+result.getTotalPages());
        log.info("page number: "+result.getNumber());
        log.info("page size: "+result.getSize());

        List<Board> list = result.getContent();
        list.forEach(board-> log.info(board));
//        Hibernate:
//        select
//        count(b1_0.bno)
//        from
//        board b1_0
    }

//    @Test
//    public void testSearch1(){
//        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
//        boardRepository.search1(pageable);
//
//    }

//    @Test
//    public void testSearchAll(){
//        String[] types = {"t","c","w"};
//        String keyword = "3";
//        Pageable pageable = PageRequest.of(1,10, Sort.by("bno").descending());
//        Page<Board> result = boardRepository.searchAll(types,keyword,pageable);
//
//
//        log.info(result.getTotalPages()); // total pages
//        log.info(result.getSize()); // page size(한페이지)
//        log.info(result.getNumber()); // page Number
//        log.info(result.hasPrevious() +"__"+ result.hasNext()); // 이전페이지 유무(boolean) 다음페이지 유무(boolean)
//
//        result.getContent().forEach(board -> log.info(board));
//
//    }

//    @Test
//    public void testSearchReplyCount(){
//        String[] types = {"t","c","w"};
//        String keyword = "6";
//        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
//        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);
//
//        log.info(result.getTotalPages()); // total pages
//        log.info(result.getSize()); // page size(한페이지)
//        log.info(result.getNumber()); // page Number
//        log.info(result.hasPrevious() +"__"+ result.hasNext()); // 이전페이지 유무(boolean) 다음페이지 유무(boolean)
//
//        result.getContent().forEach(board -> log.info(board));
//    }

    @Test
    public void testInsertWithImage(){
        Board board = Board.builder()
                .id("tester01")
                .title("Image test")
                .content("첨부파일 테스트")
                .name("tester01")
                .build();

        for(int i=0; i<3; i++){
            board.addImage(UUID.randomUUID().toString(), "file"+i+".jpg");

        }

        boardRepository.save(board);
    }

    @Test
    public void testReadWithImage(){
        Optional<Board> result = boardRepository.findByIdWithImages(102L);
        Board board = result.orElseThrow();

        log.info(board);
        for(BoardImage boardImage : board.getImageSet()){
            log.info(boardImage);
        }
    }

    @Test
    public void testModifyImage(){
        Optional<Board> result = boardRepository.findByIdWithImages(97L);
        Board board = result.orElseThrow();
        //기존 첨부파일을 삭제
        board.clearImage();
        //새로운 첨부파일 등록
        for(int i=0; i<2; i++){
            board.addImage(UUID.randomUUID().toString(), "updateFile"+i+".jpg");
        }

        boardRepository.save(board);
    }

//    @Test
//    @Transactional
//    @Commit
//    public void testRemoveAll(){
//        Long bno = 98L;
//        replyRepository.deleteByBoard_Bno(bno);
//        boardRepository.deleteById(bno);
//    }
}
