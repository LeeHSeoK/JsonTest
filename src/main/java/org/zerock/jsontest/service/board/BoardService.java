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
    void incrementViewCount(Long bno);
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

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
                .likeCount(boardDTO.getLikeCount())  // 좋아요 수 추가
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
                .likeCount(board.getLikeCount())  // 좋아요 수 추가
                .build();

        List<String> fileNames = board.getImageSet().stream().sorted().map(boardImage ->
                boardImage.getUuid() + "_" + boardImage.getFileName()).collect(Collectors.toList());

        boardDTO.setFileNames(fileNames);
        return boardDTO;
    }
}
