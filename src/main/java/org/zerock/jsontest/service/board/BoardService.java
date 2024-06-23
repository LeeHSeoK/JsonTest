package org.zerock.jsontest.service.board;

import org.zerock.jsontest.domain.board.Board;
import org.zerock.jsontest.dto.board.*;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {
    Long register(BoardDTO boardDTO);
    BoardDTO readOne(Long bno);
    void modify(BoardDTO boardDTO);
    void remove(Long bno);
//    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    void incrementViewCount(Long bno);

    //댓글의 갯수까지 처리
//    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    //게시글의 이미지, 댓글의 갯수(숫자) 처리
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    //mapping이 복잡할때 메서드를 생성해서 맵핑하는게 효율적이다.

    default Board dtoToEntity(BoardDTO boardDTO){
        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .id(boardDTO.getId())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .name(boardDTO.getName())
                .xaxis(boardDTO.getXaxis())
                .yaxis(boardDTO.getYaxis())
                .placeName(boardDTO.getPlaceName())
                .viewCount(boardDTO.getViewCount())
                .build();

        if(boardDTO.getFileNames() != null){
            boardDTO.getFileNames().forEach(fileName ->{
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        }

        return board;
    }

    default BoardDTO entityToDTO(Board board){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .id(board.getId())
                .title(board.getTitle())
                .content(board.getContent())
                .name(board.getName())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .xaxis(board.getXaxis())
                .yaxis(board.getYaxis())
                .placeName(board.getPlaceName())
                .viewCount(board.getViewCount())
                .build();

        List<String> fileNames = board.getImageSet().stream().sorted().map(boardImage ->
                boardImage.getUuid()+"_"+boardImage.getFileName()).collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);
        return boardDTO;
    }
}
